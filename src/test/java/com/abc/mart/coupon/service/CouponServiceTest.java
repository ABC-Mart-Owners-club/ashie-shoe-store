package com.abc.mart.coupon.service;

import com.abc.mart.coupon.domain.Coupon;
import com.abc.mart.coupon.domain.CouponRepository;
import com.abc.mart.coupon.domain.CouponType;
import com.abc.mart.coupon.domain.policy.StockCouponPolicy;
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
        var coupon1 = Coupon.createStockCoupon("coupon1", "30일 재고 쿠폰", CouponType.STOCK_DISCOUNT,
                10, stockPolicy30Days, LocalDateTime.now()); // 10% 할인
        var coupon2 = Coupon.createStockCoupon("coupon2", "60일 재고 쿠폰", CouponType.STOCK_DISCOUNT,
                20, stockPolicy60Days, LocalDateTime.now()); // 20% 할인

        // 재고 생성
        var stocks = List.of(
                Stock.of("1",LocalDateTime.now().minusDays(25)), // 30일 이내
                Stock.of("2",LocalDateTime.now().minusDays(50)), // 60일 이내
                Stock.of("3",LocalDateTime.now().minusDays(70))  // 60일 초과
        );

        when(couponRepository.findStockCouponIssuedToMember(memberId)).thenReturn(List.of(coupon1, coupon2));

        // when
        var result = couponService.applyStockCouponAndReturnDiscountedAmount(memberId, stocks, productPrice);

        // then
        assertEquals(3000L, result); // 10% + 20% 할인 적용
    }
}