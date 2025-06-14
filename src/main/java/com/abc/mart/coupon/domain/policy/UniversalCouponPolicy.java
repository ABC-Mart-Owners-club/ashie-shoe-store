package com.abc.mart.coupon.domain.policy;

import com.abc.mart.coupon.domain.CouponType;

public final class UniversalCouponPolicy implements CouponPolicy {
    @Override
    public CouponType getCouponType() {
        return CouponType.UNIVERSAL_DISCOUNT;
    }
    //This class doesn't have any fields or methods yet, but it can be extended in the future
    //Universal is applied for total amount of purchase
}
