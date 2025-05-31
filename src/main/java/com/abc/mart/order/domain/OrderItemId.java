package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.ValueObject;

@ValueObject
public record OrderItemId(
        String id
){

    public static OrderItemId of(String id) {
        return new OrderItemId(id);
    }

    public static OrderItemId generate(OrderId orderId,String productId, int sequence){
        return new OrderItemId(orderId.id() +"-" + productId+"-"+sequence);
    }
}
