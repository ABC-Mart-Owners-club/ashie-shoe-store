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
        var products = List.of(Product.of(productId1, "productName", price1), Product.of(productId2, "productName", price2));

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var order1 = Order.createOrder(customer);
        order1.setOrderItems(List.of(OrderItem.of(products.getFirst(), 10, order1.getOrderId(), 1),
                OrderItem.of(products.getLast(), 3, order1.getOrderId(), 2)));

        var order2 = Order.createOrder(customer);
        order2.setOrderItems(List.of(
                OrderItem.of(products.getFirst(), 7, order2.getOrderId(), 1),
                OrderItem.of(products.getLast(), 2, order2.getOrderId(), 2)
        ));

        var order3 = Order.createOrder(customer);
        order3.setOrderItems(List.of(
                OrderItem.of(products.getFirst(), 6, order3.getOrderId(), 1),
                OrderItem.of(products.getLast(), 2, order3.getOrderId(), 2)
        ));

        var from = LocalDateTime.now().minusMonths(1);
        var to = LocalDateTime.now();

        when(orderRepository.findByTerm(any())).thenReturn(List.of(order1, order2, order3));


        //when
        var calculateSalesAmountRequest = CalculateSalesRequest.builder()
                .productId(productId1).from(from).to(to).build();

        var res = calculateSalesAmountUsecase.calculateSalesAmount(calculateSalesAmountRequest);

        //then
        assertEquals(230000, res);

    }

}