package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import com.abc.mart.product.domain.Product;
import com.abc.mart.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CancelOrderUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    CancelOrderUsecase usecase = new CancelOrderUsecase(orderRepository, productRepository);

    @Test
    void cancelOrder() {
        //given
        var productId1 = "productId1";
        var productId2 = "productId2";
        var price1 = 10000;
        var price2 = 20000;

        var orderMemberId = "memberId";
        var products = List.of(Product.of(productId1, "productName", price1, 10, true),
                Product.of(productId2, "productName", price2, 5, true));

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var order = Order.createOrder(
                List.of(OrderItem.of(products.getFirst(), 10), OrderItem.of(products.getLast(), 3)), customer
        );

        var now = LocalDateTime.now();
        var orderId = OrderId.generate(orderMemberId, now);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(productRepository.findByProductIdAndIsAvailable(productId1, true)).thenReturn(Optional.of(products.getFirst()));
        when(productRepository.findByProductIdAndIsAvailable(productId2, true)).thenReturn(Optional.of(products.getLast()));

        //when
        var res = usecase.cancelOrder(orderId.id());

        //then
        //재고 변경은 로그로 확인 가능
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId1).getOrderState());
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId2).getOrderState());
    }

}