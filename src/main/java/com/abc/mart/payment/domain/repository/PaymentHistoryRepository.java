package com.abc.mart.payment.domain.repository;

import com.abc.mart.order.domain.OrderId;
import com.abc.mart.payment.domain.PaymentHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository {
    void save(PaymentHistory paymentHistory);
    PaymentHistory findByOrderId(OrderId orderId);
}
