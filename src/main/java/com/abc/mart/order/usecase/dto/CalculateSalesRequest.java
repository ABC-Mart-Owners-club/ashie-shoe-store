package com.abc.mart.order.usecase.dto;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CalculateSalesRequest(
        LocalDateTime from,
        LocalDateTime to,
        String productId
) {
}
