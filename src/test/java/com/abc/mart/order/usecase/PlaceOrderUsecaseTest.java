package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.member.domain.repository.MemberRepository;
import com.abc.mart.order.domain.OrderItemState;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaceOrderUsecaseTest {

    MemberRepository memberRepository = mock(MemberRepository.class);
    OrderRepository orderRepository = mock(OrderRepository.class);
    ProductRepository productRepository = mock(ProductRepository.class);

    PlaceOrderUsecase placeOrderUsecase = new PlaceOrderUsecase(productRepository, memberRepository, orderRepository);

    @Test
    public void placeOrderTest() {
        //given
        var productId1 = "productId1";
        var productId2 = "productId2";
        var price1 = 10000;
        var price2 = 20000;

        var orderMemberId = "memberId";

        var orderRequest = OrderRequest.builder()
                .orderItemRequestList(
                        List.of(OrderRequest.OrderItemRequest.builder()
                                .productId(productId1).quantity(1).build(),
                                OrderRequest.OrderItemRequest.builder().productId(productId2).
                                        quantity(3).build())
                ).orderMemberId(orderMemberId).build();

        var member = Member.of(orderMemberId, "memberName", "email", "phoneNum");
        var productIds = List.of(productId1, productId2);
        var products = List.of(Product.of(productId1, "productName", price1), Product.of(productId2, "productName", price2));

        when(memberRepository.findByMemberId(orderMemberId)).thenReturn(member);
        when(productRepository.findByProductId(productIds)).thenReturn(products);

        //when
        var result = placeOrderUsecase.placeOrder(orderRequest);

        //then
        assertEquals(70000, result.calculateTotalPrice());
        assertEquals(OrderItemState.PREPARING, result.getOrderItems().get(productId1).getOrderItemState());
        assertEquals(OrderItemState.PREPARING, result.getOrderItems().get(productId2).getOrderItemState());
        assertEquals(10000, result.getOrderItems().get(productId1).getTotalPrice());
        assertEquals(60000, result.getOrderItems().get(productId2).getTotalPrice());
    }

}