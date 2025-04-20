package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.domain.repository.OrderSpecification;
import com.abc.mart.order.usecase.dto.CalculateSalesRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateSalesAmountUsecase {

    private final OrderRepository orderRepository;

    @Transactional
    public long calculateSalesAmount(CalculateSalesRequest request) {
        var orders = orderRepository.findByTerm(OrderSpecification.between(request.from(), request.to()));

        if (orders.isEmpty()) return 0;

        return orders.stream()
                .mapToLong(o -> o.calculateSalesAmountOfSpecificProduct(request.productId()))
                .sum();
    }
}
