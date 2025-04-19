package com.abc.mart.order.usecase.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CalcuateSalesAmountRequest(
        String orderId,
        String productId
) {
}
