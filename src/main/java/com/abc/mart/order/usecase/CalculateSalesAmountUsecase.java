package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.CalcuateSalesAmountRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateSalesAmountUsecase {

    private final OrderRepository orderRepository;

    @Transactional
    public long calculateSalesAmount(CalcuateSalesAmountRequest request){
        var order = orderRepository.findById(OrderId.of(request.orderId()));

        return order.calculateSalesAmountOfSpecificProduct(request.productId());
    }
}
