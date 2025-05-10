package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.ValueObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ValueObject
public record OrderId (
        String id
){

    public static OrderId of(String id) {
        return new OrderId(id);
    }

    public static OrderId generate(String customerId, LocalDateTime createdDt){
        String ts = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(createdDt);
        String randomId = UUID.randomUUID().toString().replace("-", "");
        return new OrderId(customerId + "-" + ts + "-" + randomId);
    }
}
