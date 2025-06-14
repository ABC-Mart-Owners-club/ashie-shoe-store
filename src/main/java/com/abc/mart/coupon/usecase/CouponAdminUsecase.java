package com.abc.mart.coupon.usecase;

import com.abc.mart.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponAdminUsecase {
    private final CouponService couponService;

    public long countUsedQuantityByEachCoupon(String couponId){
        return couponService.countUsedQuantityByEachCoupon(couponId);
    }
}
