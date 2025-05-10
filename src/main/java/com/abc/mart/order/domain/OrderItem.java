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
    OrderItemState orderItemState;

    public static OrderItem of(Product product, int quantity, OrderId orderId, int sequence){
        var orderItem = new OrderItem();
        orderItem.orderItemId = OrderItemId.generate(orderId, product.getId(), sequence);
        orderItem.productId = product.getId();
        orderItem.orderedPrice = product.getPrice();
        orderItem.quantity = quantity;
        orderItem.orderItemState = OrderItemState.PREPARING;

        return orderItem;
    }

    public void cancelOrderItem(){
        if(OrderItemState.SHIPPED.equals(orderItemState) || OrderItemState.DELIVERED.equals(orderItemState)){
            throw new RuntimeException("OrderItem is already " + orderItemState.name());
        }

        this.orderItemState = OrderItemState.CANCELLED;
    }

    public Long getTotalPrice(){
        return quantity * orderedPrice;
    }
}
