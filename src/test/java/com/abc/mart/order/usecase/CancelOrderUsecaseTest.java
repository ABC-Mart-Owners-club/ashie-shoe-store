package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.product.domain.Product;
import com.abc.mart.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.abc.mart.test.TestStubCreator.generateRandomStocks;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        var product1Stocks = generateRandomStocks(15); // 재고 15개 생성
        var product2Stocks = generateRandomStocks(10); // 재고 10개 생성

        var products = List.of(
                Product.of(productId1, "productName1", price1, product1Stocks, true),
                Product.of(productId2, "productName2", price2, product2Stocks, true)
        );

        var customer = Customer.from(Member.of(orderMemberId, "memberName", "email", "phoneNum"));

        var orderItem1 = OrderItem.of(products.get(0), 10);
        orderItem1.setStockIds(List.of("1","2", "3", "4", "5", "6", "7", "8", "9", "10"));

        var orderItem2 = OrderItem.of(products.get(1), 5);
        orderItem2.setStockIds(List.of("11","12","13","14","15"));

        var order = Order.createOrder(
                List.of(orderItem1, orderItem2), customer
        );

        var now = LocalDateTime.now();
        var orderId = OrderId.generate(orderMemberId, now);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(productRepository.findByProductIdAndIsAvailable(productId1, true)).thenReturn(Optional.of(products.get(0)));
        when(productRepository.findByProductIdAndIsAvailable(productId2, true)).thenReturn(Optional.of(products.get(1)));

        //when
        var res = usecase.cancelOrder(orderId.id());

        //then
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId1).getOrderState());
        assertEquals(OrderItemState.CANCELLED, res.getOrderItems().get(productId2).getOrderState());
    }

}