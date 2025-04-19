package com.abc.mart.order.domain.repository;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    void placeOrder(Order order);
    Order findById(OrderId orderId);
}
