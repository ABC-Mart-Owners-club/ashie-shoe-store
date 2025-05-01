package com.abc.mart.order.usecase.dto;

import com.abc.mart.payment.infra.PaymentMethodType;
import lombok.Builder;

import java.util.List;

@Builder
public record PartialOrderCancelRequest(
        String orderId,
        List<String> cancelProductIds,
        List<PartialPaymentCancelRequest> partialPaymentCancelRequests
) {
    @Builder
    public record PartialPaymentCancelRequest(
            PaymentMethodType paymentMethodType,
            Long cancelledAmountByPaymentMethod
    ){

    }
}
