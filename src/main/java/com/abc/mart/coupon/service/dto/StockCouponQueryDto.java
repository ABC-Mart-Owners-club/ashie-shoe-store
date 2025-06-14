package com.abc.mart.coupon.service.dto;

import com.abc.mart.coupon.domain.policy.StockCouponPolicy;
import lombok.Getter;

public record StockCouponQueryDto (
        String memberIssuedCouponId,
        StockCouponPolicy stockCouponPolicy,
        int discountRate
){

}
