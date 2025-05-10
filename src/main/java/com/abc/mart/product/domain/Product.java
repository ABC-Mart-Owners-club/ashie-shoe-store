package com.abc.mart.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {

    private String id;
    private String name;
    private long price;
    private int stockCount;
    private boolean isAvaliable;

    public static Product of(String id, String name, long price, int stockCount, boolean isOnSale) {
        return new Product(id, name, price, stockCount, isOnSale);
    }

    public void addStock(int quantity){
        this.stockCount += quantity;
    }

    public void subtractStock(int quantity){
        if (this.stockCount < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stockCount -= quantity;
    }

    public void manageProductAvailability(boolean isAvailable) {
        this.isAvaliable = isAvailable;
    }
}
