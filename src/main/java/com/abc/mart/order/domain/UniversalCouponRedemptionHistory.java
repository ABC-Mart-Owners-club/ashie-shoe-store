package com.abc.mart.order.domain;


public final class UniversalCouponRedemptionHistory implements CouponRedemptionHistory {

    private long discountedPrice;
    private String orderId;

    public static CouponRedemptionHistory create(long discountedPrice, String orderId){
        UniversalCouponRedemptionHistory history = new UniversalCouponRedemptionHistory();
        history.discountedPrice = discountedPrice;
        history.orderId = orderId;
        return history;
    }

    @Override
    public long getDiscountedPrice() {
        return discountedPrice;
    }
}
