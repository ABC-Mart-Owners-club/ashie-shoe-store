package com.abc.mart.order.usecase;

import com.abc.mart.coupon.service.CouponService;
import com.abc.mart.member.domain.Member;
import com.abc.mart.member.domain.repository.MemberRepository;
import com.abc.mart.order.domain.OrderItemState;
import com.abc.mart.order.domain.StockCouponRedemptionHistory;
import com.abc.mart.order.domain.UniversalCouponRedemptionHistory;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.aop.scope.ScopedObject;

import java.util.List;

import static com.abc.mart.test.TestStubCreator.generateRandomStocks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaceOrderUsecaseTest {

    MemberRepository memberRepository = mock(MemberRepository.class);
    OrderRepository orderRepository = mock(OrderRepository.class);
    ProductRepository productRepository = mock(ProductRepository.class);
    CouponService couponService = mock(CouponService.class);

    PlaceOrderUsecase placeOrderUsecase = new PlaceOrderUsecase(productRepository,
            memberRepository, orderRepository,couponService);

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
        var products = List.of(Product.of(productId1, "productName", price1, generateRandomStocks(10), true), Product.of(productId2, "productName", price2,
                generateRandomStocks(5), true));

        when(memberRepository.findByMemberId(orderMemberId)).thenReturn(member);
        when(productRepository.findAllByProductIdAndIsAvailable(productIds, true)).thenReturn(products);
        when(couponService.applyStockCouponAndReturnDiscountedAmount(anyString(), anyList(), anyLong()))
                .thenReturn(5000L);
        when(couponService.applyUniversalCouponAndReturnDiscountedAmount(anyString(), anyLong()))
                .thenReturn(6000L);

        //when
        var result = placeOrderUsecase.placeOrder(orderRequest);

        //then
        var resOrder = result.getLeft();
        var resProducts = result.getRight();
        assertEquals(54000, resOrder.calculateTotalPrice());
        assertEquals(16000, resOrder.calculateTotalDiscountedAmount());
        assertEquals(OrderItemState.PREPARING, resOrder.getOrderItems().get(productId1).getOrderState());
        assertEquals(OrderItemState.PREPARING, resOrder.getOrderItems().get(productId2).getOrderState());
        assertEquals(10000, resOrder.getOrderItems().get(productId1).getTotalPrice());
        assertEquals(60000, resOrder.getOrderItems().get(productId2).getTotalPrice());
        assertEquals(3, resOrder.getCouponRedemptionHistories().size());
        assertInstanceOf(StockCouponRedemptionHistory.class, resOrder.getCouponRedemptionHistories().getFirst());
        assertInstanceOf(UniversalCouponRedemptionHistory.class, resOrder.getCouponRedemptionHistories().getLast());
        assertEquals(9, resProducts.get(productId1).getStocks().stream().filter(s -> !s.isSold()).toList().size());
        assertEquals(2, resProducts.get(productId2).getStocks().stream().filter(s -> !s.isSold()).toList().size());
    }



}