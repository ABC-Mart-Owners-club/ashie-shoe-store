package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.AggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AggregateRoot
public class Order {

    @Getter
    private OrderId orderId;

    @Getter
    private Map<String, OrderItem> orderItems;
    @Getter
    private OrderStatus orderStatus;
    private Customer customer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order createOrder(List<OrderItem> orderItems, Customer customer) {
        Order order = new Order();
        var now = LocalDateTime.now();

        OrderId id = OrderId.generate(customer.memberId, now);
        order.orderId = id;

        order.setOrderItems(orderItems);
        order.customer = customer;

        order.orderStatus = OrderStatus.REQUESTED;

        order.createdAt = now;
        order.updatedAt = now;

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

    public List<OrderItem> partialCancelOrder(List<String> cancelledItemIds) {
        List<OrderItem> cancelledItems = new ArrayList<>();
        for(var cancelledOrderId : cancelledItemIds){
            orderItems.get(cancelledOrderId).cancelOrderItem();
            cancelledItems.add(orderItems.get(cancelledOrderId));
        }
        return cancelledItems;
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
