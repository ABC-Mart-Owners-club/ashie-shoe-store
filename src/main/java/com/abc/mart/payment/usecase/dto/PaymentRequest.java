package com.abc.mart.payment.usecase.dto;


import com.abc.mart.payment.infra.PaymentMethodType;

import java.util.List;

public record PaymentRequest(
        String orderItemId,
        List<PaymentDetailRequest> paymentDetailRequests
) {

    public long calculateTotalRequestedPaymentAmount(){
        return paymentDetailRequests.stream().mapToLong(PaymentDetailRequest::payedAmount).sum();
    }

    public record PaymentDetailRequest(
            PaymentMethodType paymentMethodType,
            long payedAmount
    ){

    }
}
