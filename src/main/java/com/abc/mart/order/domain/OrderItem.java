package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.Entity;
import com.abc.mart.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {

    OrderItemId orderItemId;
    @Getter
    String productId;
    Long orderedPrice; //snapshot of the price when the order was placed
    int quantity;

    @Getter
    OrderState orderState;

    public static OrderItem of(Product product, int quantity, OrderId orderId, int sequence){
        var orderItem = new OrderItem();
        orderItem.orderItemId = OrderItemId.generate(orderId, product.getId(), sequence);
        orderItem.productId = product.getId();
        orderItem.orderedPrice = product.getPrice();
        orderItem.quantity = quantity;
        orderItem.orderState = OrderState.PREPARING;

        return orderItem;
    }

    public void cancelOrderItem(){
        this.orderState = OrderState.CANCELLED;
    }

    public Long getTotalPrice(){
        return quantity * orderedPrice;
    }
}
