package com.abc.mart.payment.infra;

import com.abc.mart.payment.domain.PaymentMethod;
import com.abc.mart.payment.domain.PaymentProcessState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.abc.mart.payment.infra.VisaCardPaymentMethod.VisaCardPaymentProcessState.VISA_CARD_PAYMENT_SUCCESS;

@Slf4j
@Component
public class VisaCardPaymentMethod implements PaymentMethod {

    @Override
    public PaymentMethodType getPaymentMethodType() {
        return PaymentMethodType.VISA_CARD;
    }

    @Override
    public PaymentProcessState process(long paymentAmount) {
        log.info("processing cash payment method : {}", paymentAmount);

        return switch(callVisaCardApi()){
            //외부 카드사 api에서 사용하는 상태값을 우리가 사용하는 상태값으로 치환하는 프로세스
            case VISA_CARD_PAYMENT_SUCCESS -> PaymentProcessState.APPROVED;
            case VISA_CARD_PAYMENT_FAILURE -> PaymentProcessState.FAILED;
        };
    }


    public VisaCardPaymentProcessState callVisaCardApi(){
        log.info("call visa card api");
        return VISA_CARD_PAYMENT_SUCCESS;
    }

    enum VisaCardPaymentProcessState {
        VISA_CARD_PAYMENT_FAILURE, VISA_CARD_PAYMENT_SUCCESS
    }
}
