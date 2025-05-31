package com.abc.mart.product.usecase.dto;

public record ResFetchStock(
        String productId,
        String productName,
        int stockCount
) {
}
