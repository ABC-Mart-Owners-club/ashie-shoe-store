package com.abc.mart.payment.domain;

import com.abc.mart.common.annotation.AggregateRoot;
import com.abc.mart.order.domain.OrderId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@AggregateRoot
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class PaymentHistory {
    PaymentHistoryId id;
    OrderId orderId;
    List<PaymentDetail> paymentDetails;
    long totalPayedAmount;
    PaymentProcessState processState;
    LocalDateTime createdDt;
    LocalDateTime updatedDt;

    public static PaymentHistory create(OrderId orderId, long totalPayedAmount,
                                 List<PaymentDetail> paymentDetails, LocalDateTime createdDt) {

        if (paymentDetails.stream().mapToLong(PaymentDetail::getTotalPayedAmount).sum() != totalPayedAmount) {
            throw new RuntimeException("Payment total amount does not match");
        }


        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.id = PaymentHistoryId.generate(orderId);
        paymentHistory.orderId = orderId;
        paymentHistory.paymentDetails = paymentDetails;
        paymentHistory.totalPayedAmount = totalPayedAmount;
        paymentHistory.createdDt = createdDt;
        paymentHistory.updatedDt = createdDt;

        return paymentHistory;
    }

}
