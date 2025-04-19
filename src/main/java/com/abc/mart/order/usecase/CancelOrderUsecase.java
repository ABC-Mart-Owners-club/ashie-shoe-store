package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
