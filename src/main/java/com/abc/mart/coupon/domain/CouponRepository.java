package com.abc.mart.coupon.domain;

import com.abc.mart.coupon.service.dto.StockCouponQueryDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository {
    List<StockCouponQueryDto> findStockCouponIssuedToMember(String memberId); //search for a stock coupon issued to a member
    Coupon findHighestDiscountRateUniversalCouponsIssuedToMember(String memberId);

    List<MemberIssuedCoupon> findIssuedCouponsByCouponId(String couponId);
}
