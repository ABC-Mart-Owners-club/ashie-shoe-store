package com.abc.mart.coupon.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository {
    List<Coupon> findStockCouponIssuedToMember(String memberId); //search for a stock coupon issued to a member
    Coupon findHighestDiscountRateUniversalCouponsIssuedToMember(String memberId);
}
