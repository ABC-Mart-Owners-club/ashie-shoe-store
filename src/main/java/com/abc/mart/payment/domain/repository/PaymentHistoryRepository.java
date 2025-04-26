package com.abc.mart.payment.domain.repository;

import com.abc.mart.payment.domain.PaymentHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository {
    void save(PaymentHistory paymentHistory);
}
