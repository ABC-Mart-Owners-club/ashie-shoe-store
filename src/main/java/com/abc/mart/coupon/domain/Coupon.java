package com.abc.mart.coupon.domain;

import com.abc.mart.coupon.domain.policy.CouponPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Coupon {
    private String id;
    private String name;
    private CouponType couponType;
    private int discountRate;
    private CouponPolicy couponPolicy;
    private LocalDateTime expiredAt;

    public static Coupon createStockCoupon(String id, String name, CouponType couponType,
                                           int discountRate, CouponPolicy couponPolicy, LocalDateTime expiredAt) {
        return new Coupon(id, name, couponType, discountRate, couponPolicy, expiredAt);
    }
}
