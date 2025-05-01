package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.AggregateRoot;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static Order createOrder(Customer customer, Map<String, Product> productMap, List<OrderRequest.OrderItemRequest> orderItemRequests) {
        Order order = new Order();
        var now = LocalDateTime.now();

        OrderId id = OrderId.generate(customer.memberId, now);
        order.orderId = id;
        order.customer = customer;
        order.createdAt = now;
        order.updatedAt = now;
        order.orderStatus = OrderStatus.REQUESTED;

        order.orderItems = orderItemRequests.stream().map(orderItemRequest -> {
            var product = productMap.get(orderItemRequest.productId());
            var quantity = orderItemRequest.quantity();
            return OrderItem.of(product, quantity, order.getOrderId(), orderItemRequest.sequence());
        }).collect(Collectors.toMap(OrderItem::getProductId, Function.identity()));

        return order;
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
        return this.orderItems.values().stream().filter(orderItem -> !OrderItemState.CANCELLED.equals(orderItem.getOrderItemState())).mapToLong(OrderItem::getTotalPrice).sum();
    }

    public void orderGetPaid() {
        this.orderStatus = OrderStatus.PAID;
    }

}
