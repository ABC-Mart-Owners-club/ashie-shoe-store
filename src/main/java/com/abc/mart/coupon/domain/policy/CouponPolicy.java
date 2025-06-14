package com.abc.mart.coupon.domain.policy;

import com.abc.mart.coupon.domain.CouponType;

public sealed interface CouponPolicy permits UniversalCouponPolicy, StockCouponPolicy {
    CouponType getCouponType(); // Returns the type of coupon this policy applies to
}
