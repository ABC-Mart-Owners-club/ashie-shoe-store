package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.CalculateSalesRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateSalesAmountUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    CalculateSalesAmountUsecase calculateSalesAmountUsecase = new CalculateSalesAmountUsecase(orderRepository);

    @Test
    void CalculateSalesAmountUsecaseTest() {
        // given
        var productId1 = "productId1";
        var productId2 = "productId2";
        var price1 = 10000;
        var price2 = 20000;

        var orderMemberId = "memberId";
        var products = List.of(
                Product.of(productId1, "productName1", price1, 10, true),
                Product.of(productId2, "productName2", price2, 5, true)
        );

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var orderItems1 = List.of(
                OrderItem.of(products.get(0), 10),
                OrderItem.of(products.get(1), 3)
        );
        var order1 = Order.createOrder(orderItems1, customer);

        var orderItems2 = List.of(
                OrderItem.of(products.get(0), 7),
                OrderItem.of(products.get(1), 2)
        );
        var order2 = Order.createOrder(orderItems2, customer);

        var orderItems3 = List.of(
                OrderItem.of(products.get(0), 6),
                OrderItem.of(products.get(1), 2)
        );
        var order3 = Order.createOrder(orderItems3, customer);

        var from = LocalDateTime.now().minusMonths(1);
        var to = LocalDateTime.now();

        when(orderRepository.findByTerm(any(), any())).thenReturn(List.of(order1, order2, order3));

        // when
        var calculateSalesAmountRequest = CalculateSalesRequest.builder()
                .productId(productId1).from(from).to(to).build();

        var res = calculateSalesAmountUsecase.calculateSalesAmount(calculateSalesAmountRequest);

        // then
        assertEquals(230000, res);
    }
}