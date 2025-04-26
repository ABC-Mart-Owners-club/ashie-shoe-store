package com.abc.mart.payment.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.OrderStatus;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.payment.domain.PaymentHistory;
import com.abc.mart.payment.domain.PaymentMethod;
import com.abc.mart.payment.domain.PaymentProcessState;
import com.abc.mart.payment.domain.repository.PaymentHistoryRepository;
import com.abc.mart.payment.infra.*;
import com.abc.mart.payment.usecase.dto.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessPaymentUsecaseTest {

    @Mock
    private PaymentMethodRegistry paymentMethodRegistry;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @InjectMocks
    private ProcessPaymentUsecase usecase;

    private PaymentMethod cashPaymentMethod;
    private PaymentMethod visaCardPaymentMethod;
    private PaymentMethod amexCardPaymentMethod;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cashPaymentMethod = mock(CashPaymentMethod.class);
        visaCardPaymentMethod = mock(VisaCardPaymentMethod.class);
        amexCardPaymentMethod = mock(AmexCardPaymentMethod.class);

        when(paymentMethodRegistry.getPaymentMethod(PaymentMethodType.CASH)).thenReturn(cashPaymentMethod);
        when(paymentMethodRegistry.getPaymentMethod(PaymentMethodType.VISA_CARD)).thenReturn(visaCardPaymentMethod);
    }

    @Test
    void processPayment_Success() {
        // given
        var orderId = OrderId.generate("memberId", LocalDateTime.now());
        var order = mock(Order.class);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(order.getOrderStatus()).thenReturn(OrderStatus.REQUESTED);
        when(order.calculateTotalPrice()).thenReturn(30000L);

        when(cashPaymentMethod.process(10000L)).thenReturn(PaymentProcessState.APPROVED);
        when(visaCardPaymentMethod.process(20000L)).thenReturn(PaymentProcessState.APPROVED);

        var paymentRequest = new PaymentRequest(
                orderId.id(),
                List.of(
                        new PaymentRequest.PaymentDetailRequest(PaymentMethodType.CASH, 10000L),
                        new PaymentRequest.PaymentDetailRequest(PaymentMethodType.VISA_CARD, 20000L)
                )
        );

        // when
        var paymentHistory = usecase.processPayment(paymentRequest);

        // then
        assertNotNull(paymentHistory);
        assertEquals(30000L, paymentHistory.getTotalPayedAmount());
        assertEquals(2, paymentHistory.getPaymentDetails().size());
        verify(paymentHistoryRepository).save(any(PaymentHistory.class));
        verify(paymentMethodRegistry).getPaymentMethod(PaymentMethodType.CASH);
        verify(paymentMethodRegistry).getPaymentMethod(PaymentMethodType.VISA_CARD);
    }

    @Test
    void processPayment_Failure_InvalidOrderStatus() {
        // given
        var orderId = OrderId.generate("memberId", LocalDateTime.now());
        var order = mock(Order.class);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(order.getOrderStatus()).thenReturn(OrderStatus.CANCELLED);

        var paymentRequest = new PaymentRequest(
                orderId.id(),
                List.of(new PaymentRequest.PaymentDetailRequest(PaymentMethodType.CASH, 10000L))
        );

        // when & then
        var exception = assertThrows(RuntimeException.class, () -> usecase.processPayment(paymentRequest));
        assertEquals("Order status should be PAYMENT_REQUESTED", exception.getMessage());
    }

    @Test
    void processPayment_Failure_InvalidPaymentAmount() {
        // given
        var orderId = OrderId.generate("memberId", LocalDateTime.now());
        var order = mock(Order.class);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(order.getOrderStatus()).thenReturn(OrderStatus.REQUESTED);
        when(order.calculateTotalPrice()).thenReturn(30000L);

        var paymentRequest = new PaymentRequest(
                orderId.id(),
                List.of(new PaymentRequest.PaymentDetailRequest(PaymentMethodType.CASH, 20000L))
        );

        // when & then
        var exception = assertThrows(IllegalArgumentException.class, () -> usecase.processPayment(paymentRequest));
        assertEquals("The requested payment amount does not meet the order amount.", exception.getMessage());
    }

    @Test
    void processPayment_Failure_PaymentMethodDeclined() {
        // given
        var orderId = OrderId.generate("memberId", LocalDateTime.now());
        var order = mock(Order.class);
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(order.getOrderStatus()).thenReturn(OrderStatus.REQUESTED);
        when(order.calculateTotalPrice()).thenReturn(30000L);

        when(cashPaymentMethod.process(30000L)).thenReturn(PaymentProcessState.FAILED);

        var paymentRequest = new PaymentRequest(
                orderId.id(),
                List.of(new PaymentRequest.PaymentDetailRequest(PaymentMethodType.CASH, 30000L))
        );

        // when & then
        var exception = assertThrows(RuntimeException.class, () -> usecase.processPayment(paymentRequest));
        assertEquals("Payment processing failed by payment method CASH", exception.getMessage());
    }
}