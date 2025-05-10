package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;

import com.abc.mart.payment.domain.PaymentHistory;
import com.abc.mart.payment.domain.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartialCancelOrderUsecase {

    private final OrderRepository orderRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    public Pair<Order, PaymentHistory> cancelPartialOrder(PartialOrderCancelRequest request){
        var order = orderRepository.findById(OrderId.of(request.orderId()));
        order.partialCancelOrder(request.cancelProductIds());

        var paymentHistory = paymentHistoryRepository.findByOrderId(OrderId.of(request.orderId()));
        paymentHistory.cancelPartialPayment(request.partialPaymentCancelRequests());

        orderRepository.save(order);
        paymentHistoryRepository.save(paymentHistory);

        return Pair.of(order, paymentHistory);
    }
}
