package com.abc.mart.test;

import com.abc.mart.product.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestStubCreator {

    public static List<Stock> generateRandomStocks(int count) {
        var random = new Random();
        var stocks = new ArrayList<Stock>();
        for (int i = 0; i < count; i++) {
            stocks.add(Stock.of(String.valueOf(i+1), LocalDateTime.of(
                    2025, random.nextInt(12) + 1, random.nextInt(28) + 1,
                    random.nextInt(24), random.nextInt(60), random.nextInt(60), random.nextInt(1000)
            )));
        }
        return stocks;
    }
}
