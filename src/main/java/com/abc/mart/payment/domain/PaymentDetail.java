package com.abc.mart.payment.domain;

import com.abc.mart.common.annotation.ValueObject;
import com.abc.mart.payment.infra.PaymentMethodType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@ValueObject
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class PaymentDetail {

    PaymentMethodType paymentMethodType;
    long totalPayedAmount;

    public static PaymentDetail create(PaymentMethodType paymentMethodType, long totalPayedAmount) {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.paymentMethodType = paymentMethodType;
        paymentDetail.totalPayedAmount = totalPayedAmount;
        return paymentDetail;
    }

}
