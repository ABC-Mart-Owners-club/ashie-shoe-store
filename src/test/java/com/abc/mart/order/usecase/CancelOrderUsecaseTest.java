package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CancelOrderUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    CancelOrderUsecase usecase = new CancelOrderUsecase(orderRepository);

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

        var productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

        var orderItemRequests = List.of(
                new OrderRequest.OrderItemRequest(products.get(0).getId(), 10, 1),
                new OrderRequest.OrderItemRequest(products.get(1).getId(), 3, 2)
        );

        var order = Order.createOrder(customer, productMap, orderItemRequests);

        var now = LocalDateTime.now();
        var orderId = OrderId.generate(orderMemberId, now);
        when(orderRepository.findById(orderId)).thenReturn(order);


        //when
        var res = usecase.cancelOrder(orderId.id());

        //then
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId1).getOrderItemState());
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId2).getOrderItemState());
        assertEquals(OrderStatus.CANCELLED, res.getOrderStatus());
    }

}