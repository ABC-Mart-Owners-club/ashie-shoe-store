package com.abc.mart.payment.domain;

import com.abc.mart.common.annotation.ValueObject;
import com.abc.mart.order.domain.OrderId;

@ValueObject
public record PaymentHistoryId(
        String id
){

    public static PaymentHistoryId of(String id) {
        return new PaymentHistoryId(id);
    }

    public static PaymentHistoryId generate(OrderId orderId){
        return new PaymentHistoryId(orderId+"-paymentHistory");
    }
}
