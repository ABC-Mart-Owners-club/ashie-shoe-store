package com.abc.mart.order.domain;

sealed public interface CouponRedemptionHistory
        permits UniversalCouponRedemptionHistory, StockCouponRedemptionHistory {
    long getDiscountedPrice();
}
