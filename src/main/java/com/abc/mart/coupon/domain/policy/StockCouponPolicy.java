package com.abc.mart.coupon.domain.policy;

import com.abc.mart.coupon.domain.CouponType;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//StockCouponPolicy is available after a certain number of days since the product was stocked
@AllArgsConstructor
public final class StockCouponPolicy implements CouponPolicy {
    private int availableAfterStockedDays;

    public boolean isEligibleForCoupon(LocalDateTime stockedDateTime) {
        LocalDateTime now = LocalDateTime.now();
        long daysElapsed = ChronoUnit.DAYS.between(stockedDateTime, now);
        // Check if the number of days elapsed since the stocked date is greater than or equal to the availableAfterStockedDays
        return daysElapsed >= availableAfterStockedDays;
    }

    @Override
    public CouponType getCouponType() {
        return CouponType.STOCK_DISCOUNT;
    }
}
