package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.ValueObject;
import com.abc.mart.product.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ValueObject
//Has the same lifecycle with Order entity
public class OrderItem {

    @Setter
    @Getter
    private String productId;
    @Getter
    private int quantity;

    private long regularPrice;

    private long stockDiscountedAmount;

    @Setter
    @Getter
    private List<String> stockIds;
    @Getter
    private OrderItemState orderState;

    public static OrderItem of(Product product, long regularPrice, int quantity){
        var orderItem = new OrderItem();
        orderItem.productId = product.getId();
        orderItem.quantity = quantity;
        orderItem.regularPrice = regularPrice;
        orderItem.orderState = OrderItemState.PREPARING;
        return orderItem;
    }

    public void cancelOrderItem(){
        this.orderState = OrderItemState.CANCELLED;
    }

    public void discounted(long amount){
        stockDiscountedAmount = amount;
    }

    public long getTotalPrice(){
        return regularPrice * quantity - stockDiscountedAmount;
    }
}
