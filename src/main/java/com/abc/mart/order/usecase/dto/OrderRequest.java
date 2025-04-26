package com.abc.mart.order.usecase.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(
        String orderMemberId,
        List<OrderItemRequest> orderItemRequestList
) {

    @Builder
    public record OrderItemRequest(
            String productId,
            int quantity
    ){

    }
}
