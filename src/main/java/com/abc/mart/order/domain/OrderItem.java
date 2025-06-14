package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.ValueObject;
import com.abc.mart.product.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ValueObject
//Has the same lifecycle with Order entity
public class OrderItem {

    @Getter
    private String id;

    @Setter
    @Getter
    private String productId;
    @Getter
    private int quantity;

    private long orderedPrice;

    @Setter
    @Getter
    private List<String> stockIds;
    @Getter
    private OrderItemState orderState;

    public static OrderItem of(Product product, long orderedPrice, int quantity){
        var orderItem = new OrderItem();
        orderItem.id = "orderitem" + product.getId() + System.currentTimeMillis();
        orderItem.productId = product.getId();
        orderItem.quantity = quantity;
        orderItem.orderedPrice = orderedPrice;
        orderItem.orderState = OrderItemState.PREPARING;
        return orderItem;
    }

    public void cancelOrderItem(){
        this.orderState = OrderItemState.CANCELLED;
    }

    public long getTotalPrice(){
        return orderedPrice * quantity;
    }
}
