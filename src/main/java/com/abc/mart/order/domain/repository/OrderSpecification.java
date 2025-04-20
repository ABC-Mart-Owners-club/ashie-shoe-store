package com.abc.mart.order.domain.repository;

import com.abc.mart.order.domain.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {
    public static Specification<Order> between(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDt"), from, to);

    }
}
