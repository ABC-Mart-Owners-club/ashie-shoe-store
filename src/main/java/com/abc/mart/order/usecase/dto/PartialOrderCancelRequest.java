package com.abc.mart.order.usecase.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PartialOrderCancelRequest(
        String orderId,
        List<String> cancelProductIds
) {
}
