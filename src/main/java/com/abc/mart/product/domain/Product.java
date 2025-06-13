package com.abc.mart.product.domain;

import com.abc.mart.product.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Getter
public class Product {

    private String id;
    private String name;
    private long price;
    private List<Stock> stocks;
    private boolean isAvaliable;

    public static Product of(String id, String name, long price, List<Stock> stocks, boolean isOnSale) {
        return new Product(id, name, price, stocks, isOnSale);
    }

    public void addStock(List<Stock> stocks){
        this.stocks.addAll(stocks);
    }

    public void restoreStock(List<String> stockIds){
        this.stocks.stream()
                .filter(stock -> stockIds.contains(stock.getId()))
                .forEach(Stock::setUnsold);
    }

    public List<String> subtractStock(int quantity){
        if (this.stocks.size() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        return stocks.stream()
                .filter(stock -> !stock.isSold())
                .sorted(Comparator.comparing(Stock::getLoadedAt))
                .limit(quantity)
                .map(stock -> {
                    stock.setSold();
                    return stock.getId();
                }).toList();
    }

    public void manageProductAvailability(boolean isAvailable) {
        this.isAvaliable = isAvailable;
    }
}
