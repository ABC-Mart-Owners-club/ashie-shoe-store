package com.abc.mart.payment.usecase;

import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.OrderStatus;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.payment.domain.PaymentDetail;
import com.abc.mart.payment.domain.PaymentHistory;
import com.abc.mart.payment.domain.PaymentProcessState;
import com.abc.mart.payment.domain.repository.PaymentHistoryRepository;
import com.abc.mart.payment.infra.PaymentMethodRegistry;
import com.abc.mart.payment.usecase.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@Service
public class ProcessPaymentUsecase {

    private final PaymentMethodRegistry paymentMethodRegistry;
    private final OrderRepository orderRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    public PaymentHistory processPayment(PaymentRequest paymentRequest) {

        var orderId = OrderId.of(paymentRequest.orderItemId());
        var order = orderRepository.findById(orderId);

        if(!OrderStatus.REQUESTED.equals(order.getOrderStatus())){
            throw new RuntimeException("Order status should be PAYMENT_REQUESTED");
        }

        if (order.calculateTotalPrice() != paymentRequest.calculateTotalRequestedPaymentAmount()) {
            throw new IllegalArgumentException("The requested payment amount does not meet the order amount.");
        }

        var processedPaymentDetails = paymentRequest.paymentDetailRequests().stream().map(paymentDetailRequest -> {
            var selectedPaymentMethod = paymentMethodRegistry.getPaymentMethod(paymentDetailRequest.paymentMethodType());
            var status = selectedPaymentMethod.process(paymentDetailRequest.payedAmount());

            if (!PaymentProcessState.APPROVED.equals(status)) {
                throw new RuntimeException("Payment processing failed by payment method " + paymentDetailRequest.paymentMethodType());
            }

            return PaymentDetail.create(paymentDetailRequest.paymentMethodType(), paymentDetailRequest.payedAmount());
        }).toList();


        var paymentHistory = PaymentHistory.create(orderId, paymentRequest.calculateTotalRequestedPaymentAmount(),
                processedPaymentDetails, LocalDateTime.now(ZoneId.systemDefault()));
        paymentHistoryRepository.save(paymentHistory);

        order.orderGetPaid();
        orderRepository.save(order);

        return paymentHistory;

    }
}
