package com.abc.mart.payment.infra;

import com.abc.mart.payment.domain.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//전략 패턴 활용하여 PaymentMethod들의 구현체를 동적으로 등록하고, 필요한 구현체를 꺼내 쓸 수 있도록 함
@Component
public class PaymentMethodRegistry {
    private final Map<PaymentMethodType, PaymentMethod> registry = new EnumMap<>(PaymentMethodType.class);

    public PaymentMethodRegistry(List<PaymentMethod> paymentMethods) {
        for (PaymentMethod method : paymentMethods) {
            registry.put(method.getPaymentMethodType(), method);
        }
    }

    public PaymentMethod getPaymentMethod(PaymentMethodType type) {
        PaymentMethod method = registry.get(type);
        if (method == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + type);
        }
        return method;
    }
}
