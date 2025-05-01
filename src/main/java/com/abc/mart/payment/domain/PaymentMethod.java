package com.abc.mart.payment.domain;

import com.abc.mart.payment.infra.PaymentMethodType;

public interface PaymentMethod {

    PaymentMethodType getPaymentMethodType();
    PaymentProcessState process(long paymentAmount);
    PaymentProcessState cancel(long cancelledAmount);

}
