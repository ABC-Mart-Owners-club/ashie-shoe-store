package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.AggregateRoot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AggregateRoot
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Getter
    OrderId orderId;

    @Getter
    Map<String, OrderItem> orderItems;

    @Getter
    OrderStatus orderStatus;
    Customer customer;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static Order createOrder(Customer customer) {
        Order order = new Order();
        var now = LocalDateTime.now();

        OrderId id = OrderId.generate(customer.memberId, now);
        order.orderId = id;
        order.customer = customer;
        order.createdAt = now;
        order.updatedAt = now;
        order.orderStatus = OrderStatus.REQUESTED;
        return order;
    }

    public void setOrderItems(List<OrderItem> orderItems){
        var orderItemMap = new HashMap<String, OrderItem>();
        for (OrderItem orderItem : orderItems) {
            orderItemMap.put(orderItem.getProductId(), orderItem);
        }
        this.orderItems = orderItemMap;
    }


    public void cancelOrder() {
        for(var orderItem : orderItems.values()){
            orderItem.cancelOrderItem();
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    public void partialCancelOrder(List<String> cancelledOrderIds) {
        for(var cancelledOrderId : cancelledOrderIds){
            orderItems.get(cancelledOrderId).cancelOrderItem();
        }
    }

    public long calculateSalesAmountOfSpecificProduct(String productId){
        return this.orderItems.get(productId).getTotalPrice();
    }

    public long calculateTotalPrice() {
        return this.orderItems.values().stream().mapToLong(OrderItem::getTotalPrice).sum();
    }

    public void orderGetPaid() {
        this.orderStatus = OrderStatus.PAID;
    }

}
