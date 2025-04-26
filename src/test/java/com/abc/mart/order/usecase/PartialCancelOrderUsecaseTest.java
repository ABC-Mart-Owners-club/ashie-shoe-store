package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PartialCancelOrderUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    PartialCancelOrderUsecase usecase = new PartialCancelOrderUsecase(orderRepository);

    @Test
    void cancelPartialOrder() {
        //given
        var productId1 = "productId1";
        var productId2 = "productId2";
        var price1 = 10000;
        var price2 = 20000;

        var orderMemberId = "memberId";
        var products = List.of(Product.of(productId1, "productName", price1), Product.of(productId2, "productName", price2));

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var order = Order.createOrder(customer);
        order.setOrderItems(List.of(OrderItem.of(products.getFirst(), 10, order.getOrderId(), 1),
                OrderItem.of(products.getLast(), 3, order.getOrderId(), 2)));

        var now = LocalDateTime.now();
        var orderId = OrderId.generate(orderMemberId, now);
        when(orderRepository.findById(orderId)).thenReturn(order);


        var partialCancelRequest = PartialOrderCancelRequest.builder().orderId(orderId.id()).cancelProductIds(List.of(productId1)).build();

        //when
        var res = usecase.cancelPartialOrder(partialCancelRequest);

        //then
        assertEquals(OrderState.CANCELLED, res.getOrderItems().get(productId1).getOrderState());
        assertEquals(OrderState.PREPARING, res.getOrderItems().get(productId2).getOrderState());
    }

}