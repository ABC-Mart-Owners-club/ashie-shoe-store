package com.abc.mart.payment.infra;

import com.abc.mart.payment.domain.PaymentMethod;
import com.abc.mart.payment.domain.PaymentProcessState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CashPaymentMethod implements PaymentMethod {

    @Override
    public PaymentMethodType getPaymentMethodType() {
        return PaymentMethodType.CASH;
    }

    @Override
    public PaymentProcessState process(long paymentAmount) {
        log.info("processing cash payment method : {}", paymentAmount);
      return PaymentProcessState.APPROVED;
    }

    @Override
    public PaymentProcessState cancel(long cancelledAmount) {
        log.info("cancel cash payment method : {}", cancelledAmount);
        return PaymentProcessState.APPROVED;
    }

}
