package com.abc.mart.product;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class Stock {
    private String id;
    private LocalDateTime loadedAt;
    private boolean sold;

    public static Stock of(String id, LocalDateTime loadedAt) {
        Stock stock = new Stock();
        stock.id = id;
        stock.loadedAt = loadedAt;
        stock.sold = false;
        return stock;
    }


    public void setUnsold() {
        this.sold = false;
    }

    public void setSold() {
        this.sold = true;
    }

}
