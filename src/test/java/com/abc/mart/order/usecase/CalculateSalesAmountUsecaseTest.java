package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.CalculateSalesRequest;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateSalesAmountUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    CalculateSalesAmountUsecase calculateSalesAmountUsecase = new CalculateSalesAmountUsecase(orderRepository);

    @Test
    void CalculateSalesAmountUsecaseTest() {
        //given
        var productId1 = "productId1";
        var productId2 = "productId2";
        var price1 = 10000;
        var price2 = 20000;

        var orderMemberId = "memberId";
        var products = List.of(Product.of(productId1, "productName", price1, 10, true), Product.of(productId2, "productName", price2, 5, true));

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

        var orderItemRequests1 = List.of(
                new OrderRequest.OrderItemRequest(productId1, 10, 1),
                new OrderRequest.OrderItemRequest(productId2, 3, 2)
        );
        var order1 = Order.createOrder(customer, productMap, orderItemRequests1);

        var orderItemRequests2 = List.of(
                new OrderRequest.OrderItemRequest(productId1, 7, 1),
                new OrderRequest.OrderItemRequest(productId2, 2, 2)
        );
        var order2 = Order.createOrder(customer, productMap, orderItemRequests2);

        var orderItemRequests3 = List.of(
                new OrderRequest.OrderItemRequest(productId1, 6, 1),
                new OrderRequest.OrderItemRequest(productId2, 2, 2)
        );
        var order3 = Order.createOrder(customer, productMap, orderItemRequests3);

        var from = LocalDateTime.now().minusMonths(1);
        var to = LocalDateTime.now();

        when(orderRepository.findByTerm(any(), any())).thenReturn(List.of(order1, order2, order3));


        //when
        var calculateSalesAmountRequest = CalculateSalesRequest.builder()
                .productId(productId1).from(from).to(to).build();

        var res = calculateSalesAmountUsecase.calculateSalesAmount(calculateSalesAmountRequest);

        //then
        assertEquals(230000, res);

    }

}