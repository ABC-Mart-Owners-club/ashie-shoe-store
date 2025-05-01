package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.Member;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import com.abc.mart.payment.domain.PaymentDetail;
import com.abc.mart.payment.domain.PaymentHistory;
import com.abc.mart.payment.domain.repository.PaymentHistoryRepository;
import com.abc.mart.payment.infra.PaymentMethodType;
import com.abc.mart.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PartialCancelOrderUsecaseTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    PaymentHistoryRepository paymentHistoryRepository = Mockito.mock(PaymentHistoryRepository.class);
    PartialCancelOrderUsecase usecase = new PartialCancelOrderUsecase(orderRepository, paymentHistoryRepository);

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

        var paymentHistory = PaymentHistory.create(orderId, 160000, List.of(
                PaymentDetail.create(PaymentMethodType.CASH, 100000),
                PaymentDetail.create(PaymentMethodType.VISA_CARD, 60000)
        ), LocalDateTime.now());

        when(paymentHistoryRepository.findByOrderId(orderId)).thenReturn(paymentHistory);


        var partialCancelRequest = PartialOrderCancelRequest.builder().orderId(orderId.id()).cancelProductIds(List.of(productId1))
                .partialPaymentCancelRequests(List.of(
                        PartialOrderCancelRequest.PartialPaymentCancelRequest.builder()
                                .cancelledAmountByPaymentMethod(40000L)
                                .paymentMethodType(PaymentMethodType.CASH)
                                .build(),
                        PartialOrderCancelRequest.PartialPaymentCancelRequest.builder()
                                .cancelledAmountByPaymentMethod(60000L)
                                .paymentMethodType(PaymentMethodType.VISA_CARD)
                                .build()
                )).build();

        //when
        var res = usecase.cancelPartialOrder(partialCancelRequest);

        //then
        var resOrder = res.getLeft();
        var resPaymentHistory = res.getRight();
        assertEquals(OrderItemState.CANCELLED, resOrder.getOrderItems().get(productId1).getOrderItemState());
        assertEquals(OrderItemState.PREPARING, resOrder.getOrderItems().get(productId2).getOrderItemState());
        assertEquals(60000, resOrder.calculateTotalPrice());
        assertEquals(60000, resPaymentHistory.getTotalPayedAmount());
        assertEquals(60000L, resPaymentHistory.getPaymentDetails().get(PaymentMethodType.CASH).getPayedAmountByMethod());
        assertEquals(0L, resPaymentHistory.getPaymentDetails().get(PaymentMethodType.VISA_CARD).getPayedAmountByMethod());
    }

}