package com.abc.mart.coupon.service;

import com.abc.mart.coupon.domain.Coupon;
import com.abc.mart.coupon.domain.CouponRepository;
import com.abc.mart.coupon.domain.CouponType;
import com.abc.mart.coupon.domain.MemberIssuedCoupon;
import com.abc.mart.coupon.domain.policy.StockCouponPolicy;
import com.abc.mart.coupon.service.dto.StockCouponQueryDto;
import com.abc.mart.product.Stock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CouponServiceTest {

    CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
    CouponService couponService = new CouponService(couponRepository);

    @Test
    void applyStockCouponAndReturnDiscountedAmount_WithStockCouponPolicy() {
        // given
        var memberId = "memberId";
        var productPrice = 10000L;

        // 30일 및 60일 기준의 StockCouponPolicy 생성
        var stockPolicy30Days = new StockCouponPolicy(30);
        var stockPolicy60Days = new StockCouponPolicy(60);

        // 쿠폰 생성
        var coupon1 = new StockCouponQueryDto("coupon1", stockPolicy30Days, 10); // 10% 할인
        var coupon2 = new StockCouponQueryDto("coupon2", stockPolicy60Days, 20); // 20% 할인

        // 재고 생성
        var stocks = List.of(
                Stock.of("1", LocalDateTime.now().minusDays(25)), // 30일 이내
                Stock.of("2", LocalDateTime.now().minusDays(50)), // 60일 이내
                Stock.of("3", LocalDateTime.now().minusDays(70))  // 60일 초과
        );

        when(couponRepository.findStockCouponIssuedToMember(memberId)).thenReturn(List.of(coupon1, coupon2));

        // when
        var result = couponService.applyStockCouponAndReturnDiscountedAmount(memberId, stocks, productPrice);

        // then
        assertEquals(3000L, result); // 10% + 20% 할인 적용
    }

    @Test
    void countUsedQuantityByEachCoupon_ShouldReturnCorrectCount() {
        // given
        var couponId = "coupon123";

        var issuedCoupons = List.of(
                new MemberIssuedCoupon("member1", couponId, LocalDateTime.now(), true),
                new MemberIssuedCoupon("member2", couponId, LocalDateTime.now(), false),
                new MemberIssuedCoupon("member3", couponId, LocalDateTime.now(), true)
        );

        when(couponRepository.findIssuedCouponsByCouponId(couponId)).thenReturn(issuedCoupons);

        // when
        var result = couponService.countUsedQuantityByEachCoupon(couponId);

        // then
        assertEquals(2, result); // 사용된 쿠폰은 2개
    }


}