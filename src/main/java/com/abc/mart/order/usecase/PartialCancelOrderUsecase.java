package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartialCancelOrderUsecase {

    private final OrderRepository orderRepository;

    @Transactional
    public Order cancelPartialOrder(PartialOrderCancelRequest request){
        var order = orderRepository.findById(OrderId.of(request.orderId()));

        order.partialCancelOrder(request.cancelProductIds());

        return order;
    }
}
