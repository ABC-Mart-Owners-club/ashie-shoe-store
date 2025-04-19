package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.CalcuateSalesAmountRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

        var order = Order.createOrder(
                List.of(OrderItem.of(products.getFirst(), 10), OrderItem.of(products.getLast(), 3)), customer
        );

        var now = LocalDateTime.now();
        var orderId = OrderId.generate(orderMemberId, now);
        when(orderRepository.findById(orderId)).thenReturn(order);


        //when
        var calculateSalesAmountRequest = CalcuateSalesAmountRequest.builder().productId(productId1).orderId(orderId.id()).build();
        var res = calculateSalesAmountUsecase.calculateSalesAmount(calculateSalesAmountRequest);

        //then
        assertEquals(100000, res);

        //when
        var calculateSalesAmountRequest2 = CalcuateSalesAmountRequest.builder().productId(productId2).orderId(orderId.id()).build();
        var res2 = calculateSalesAmountUsecase.calculateSalesAmount(calculateSalesAmountRequest2);

        //then
        assertEquals(60000, res2);

    }

}