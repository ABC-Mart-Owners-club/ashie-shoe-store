package com.abc.mart.order.domain.repository;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.OrderItem;
import com.abc.mart.order.domain.OrderItemId;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository{

    void placeOrder(Order order);
    Order findById(OrderId orderId);
    OrderItem findById(OrderItemId orderItemId);

    //jpa를 아직 사용하지 않지만 spec 패턴 실습을 위해 사용
    List<Order> findByTerm(LocalDateTime from, LocalDateTime to);
    //List<Order> findByTerm(Specification spec);

    void save(Order order);
}
