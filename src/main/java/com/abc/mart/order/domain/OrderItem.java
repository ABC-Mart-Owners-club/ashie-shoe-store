package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.ValueObject;
import com.abc.mart.product.domain.Product;
import lombok.Getter;
import lombok.Setter;

@ValueObject
//Has the same lifecycle with Order entity
public class OrderItem {

    @Setter
    @Getter
    private String productId;
    private Long orderedPrice; //snapshot of the price when the order was placed
    @Getter
    private int quantity;

    @Getter
    private OrderItemState orderState;

    public static OrderItem of(Product product, int quantity){
        var orderItem = new OrderItem();
        orderItem.productId = product.getId();
        orderItem.orderedPrice = product.getPrice();
        orderItem.quantity = quantity;
        orderItem.orderState = OrderItemState.PREPARING;

        return orderItem;
    }

    public void cancelOrderItem(){
        this.orderState = OrderItemState.CANCELLED;
    }

    public Long getTotalPrice(){
        return quantity * orderedPrice;
    }
}
