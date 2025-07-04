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

import static com.abc.mart.test.TestStubCreator.generateRandomStocks;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PartialCancelOrderUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    PartialCancelOrderUsecase usecase = new PartialCancelOrderUsecase(orderRepository, productRepository);

    @Test
    void cancelPartialOrder() {
        //given
        var productId1 = "productId1";
        var productId2 = "productId2";
        var price1 = 10000;
        var price2 = 20000;

        var orderMemberId = "memberId";
        var products = List.of(Product.of(productId1, "productName", price1,
                        generateRandomStocks(10), true),
                Product.of(productId2, "productName", price2, generateRandomStocks(5), true));

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var orderItem1 = OrderItem.of(products.get(0), 10000, 10);
        orderItem1.setStockIds(List.of("1","2", "3", "4", "5", "6", "7", "8", "9", "10"));

        var orderItem2 = OrderItem.of(products.get(1), 20000, 3);
        orderItem2.setStockIds(List.of("2","3","4"));

        var order = Order.createOrder(
                List.of(orderItem1, orderItem2), customer
        );

        var now = LocalDateTime.now();
        var orderId = OrderId.generate(orderMemberId, now);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(productRepository.findByProductIdAndIsAvailable(productId1, true)).thenReturn(Optional.of(products.getFirst()));
        when(productRepository.findByProductIdAndIsAvailable(productId2, true)).thenReturn(Optional.of(products.getLast()));


        var partialCancelRequest = PartialOrderCancelRequest.builder().orderId(orderId.id()).cancelProductIds(List.of(productId1)).build();

        //when
        var res = usecase.cancelPartialOrder(partialCancelRequest);

        //then
        //재고 변경은 로그로 확인 가능
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId1).getOrderState());
        assertEquals(OrderItemState.PREPARING, res.getOrderItems().get(productId2).getOrderState());
    }

}