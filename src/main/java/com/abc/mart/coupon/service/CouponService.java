package com.abc.mart.coupon.service;

import com.abc.mart.coupon.domain.Coupon;
import com.abc.mart.coupon.domain.CouponRepository;
import com.abc.mart.coupon.domain.policy.StockCouponPolicy;
import com.abc.mart.product.Stock;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public long applyStockCouponAndReturnDiscountedAmount(String memberId, List<Stock> stocks, long productPrice) {
        // 1. Check if the member has a seller coupon issued
        // 2. If yes, apply the coupon to the stock
        // 3. Return the discounted price

        List<Coupon> coupons = couponRepository.findStockCouponIssuedToMember(memberId);

        if (coupons == null) return 0L;

        return stocks.stream().mapToLong(
                stock -> coupons.stream()
                            .filter(c -> c.getCouponPolicy() instanceof StockCouponPolicy stockPolicy
                                    && stockPolicy.isEligibleForCoupon(stock.getLoadedAt()))
                            .mapToInt(Coupon::getDiscountRate)//if several coupons are applicable, take the maximum discount rate
                            .max()
                            .orElse(0)
        ).map(maxRate -> (productPrice * maxRate) / 100).sum();
    }

    public long applyUniversalCouponAndReturnDiscountedAmount(String memberId, long currentPrice) {

        Coupon coupon = couponRepository.findHighestDiscountRateUniversalCouponsIssuedToMember(memberId);

        if (coupon == null) return 0L;

        return currentPrice * (long) coupon.getDiscountRate() / 100;
    }
}
