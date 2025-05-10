package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelOrderUsecase {

    private final OrderRepository orderRepository;

    @Transactional
    public Order cancelOrder(String orderId){
        var order = orderRepository.findById(OrderId.of(orderId));

        order.cancelOrder();
        return order;
    }
}
