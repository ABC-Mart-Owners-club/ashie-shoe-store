package com.abc.mart.payment.domain;

import com.abc.mart.common.annotation.AggregateRoot;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import com.abc.mart.payment.infra.PaymentMethodType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@AggregateRoot
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class PaymentHistory {
    PaymentHistoryId id;
    OrderId orderId;
    Map<PaymentMethodType, PaymentDetail> paymentDetails;
    long totalPayedAmount;
    PaymentProcessState processState;
    LocalDateTime createdDt;
    LocalDateTime updatedDt;

    public static PaymentHistory create(OrderId orderId, long totalPayedAmount,
                                 List<PaymentDetail> paymentDetails, LocalDateTime createdDt) {

        if (paymentDetails.stream().mapToLong(PaymentDetail::getPayedAmountByMethod).sum() != totalPayedAmount) {
            throw new RuntimeException("Payment total amount does not match");
        }


        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.id = PaymentHistoryId.generate(orderId);
        paymentHistory.orderId = orderId;
        paymentHistory.paymentDetails = paymentDetails.stream().collect(Collectors.toMap(
                PaymentDetail::getPaymentMethodType,
                paymentDetail -> paymentDetail
        ));
        paymentHistory.totalPayedAmount = totalPayedAmount;
        paymentHistory.createdDt = createdDt;
        paymentHistory.updatedDt = createdDt;

        return paymentHistory;
    }


    public void cancelPartialPayment(List<PartialOrderCancelRequest.PartialPaymentCancelRequest> requests) {
        var partialCancelledAmount = requests.stream()
                .mapToLong(PartialOrderCancelRequest.PartialPaymentCancelRequest::cancelledAmountByPaymentMethod).sum();

        if (partialCancelledAmount > totalPayedAmount) {
            throw new RuntimeException("Partial cancelled amount is greater than total payed amount");
        }

        requests.forEach(request ->
            paymentDetails.get(request.paymentMethodType()).partialCancelPayment(request.cancelledAmountByPaymentMethod())
        );

        this.totalPayedAmount -= partialCancelledAmount;
        this.updatedDt = LocalDateTime.now();

    }
}
