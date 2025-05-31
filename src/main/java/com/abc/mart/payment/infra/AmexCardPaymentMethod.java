package com.abc.mart.payment.infra;

import com.abc.mart.payment.domain.PaymentMethod;
import com.abc.mart.payment.domain.PaymentProcessState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.abc.mart.payment.infra.AmexCardPaymentMethod.AmexCardProcessState.AMEX_CARD_PROCESSING_FINISHED;

@Slf4j
@Component
public class AmexCardPaymentMethod implements PaymentMethod {

    @Override
    public PaymentMethodType getPaymentMethodType() {
        return PaymentMethodType.AMEX_CARD;
    }

    @Override
    public PaymentProcessState process(long paymentAmount) {
        log.info("processing amex payment method : {}", paymentAmount);

        return switch(callAmexCardApi()){
            //외부 카드사 api에서 사용하는 상태값을 우리가 사용하는 상태값으로 치환하는 프로세스
            case AMEX_CARD_PROCESSING_FINISHED -> PaymentProcessState.APPROVED;
            case AMEX_CARD_PROCESSING_FAILED -> PaymentProcessState.FAILED;
        };
    }

    @Override
    public PaymentProcessState cancel(long cancelledAmount) {
        log.info("cancel amex payment method : {}", cancelledAmount);

        return switch(callAmexCardApi()){
            case AMEX_CARD_PROCESSING_FINISHED -> PaymentProcessState.APPROVED;
            case AMEX_CARD_PROCESSING_FAILED -> PaymentProcessState.FAILED;
        };
    }


    public AmexCardProcessState callAmexCardApi(){
        log.info("call amex card api");

        return AMEX_CARD_PROCESSING_FINISHED;
    }

    enum AmexCardProcessState {
        AMEX_CARD_PROCESSING_FAILED, AMEX_CARD_PROCESSING_FINISHED
    }
}
