package com.abc.mart.payment.domain;

import com.abc.mart.common.annotation.ValueObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@ValueObject
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class PaymentDetail {

    PaymentMethod paymentMethod;
    long totalPayedAmount;

    public static PaymentDetail create(PaymentMethod paymentMethod, long totalPayedAmount) {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.paymentMethod = paymentMethod;
        paymentDetail.totalPayedAmount = totalPayedAmount;
        return paymentDetail;
    }

}
