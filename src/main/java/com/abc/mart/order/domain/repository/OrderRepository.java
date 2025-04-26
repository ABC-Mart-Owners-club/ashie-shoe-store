package com.abc.mart.order.domain.repository;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaSpecificationExecutor<Order> {

    void placeOrder(Order order);
    Order findById(OrderId orderId);

    //jpa를 아직 사용하지 않지만 spec 패턴 실습을 위해 사용
    List<Order> findByTerm(Specification spec);

    void save(Order order);
}
