package com.abc.mart.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Product {

    @Getter
    private String id;
    private String name;
    @Getter
    private long price;

    public static Product of(String id, String name, long price) {
        return new Product(id, name, price);
    }
}
