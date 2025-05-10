package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.CalculateSalesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CalculateSalesAmountUsecase {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public long calculateSalesAmount(CalculateSalesRequest request) {
        var orders = orderRepository.findByTerm(request.from(), request.to());

        if (orders.isEmpty()) return 0;

        return orders.stream()
                .mapToLong(o -> o.calculateSalesAmountOfSpecificProduct(request.productId()))
                .sum();
    }
}
