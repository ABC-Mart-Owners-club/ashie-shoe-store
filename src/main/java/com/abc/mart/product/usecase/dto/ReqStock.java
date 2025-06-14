package com.abc.mart.product.usecase.dto;

import java.time.LocalDateTime;

public record ReqStock(
        String stockId,
        LocalDateTime loadedAt
) {
}
