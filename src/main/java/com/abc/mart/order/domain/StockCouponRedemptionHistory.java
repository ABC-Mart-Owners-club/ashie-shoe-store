package com.abc.mart.order.domain;

public final class StockCouponRedemptionHistory implements CouponRedemptionHistory {

        private long discountedPrice;
        private String orderItemId;

        public static CouponRedemptionHistory create(String orderItemId, long discountedPrice) {
                StockCouponRedemptionHistory history = new StockCouponRedemptionHistory();
                history.orderItemId = orderItemId;
                history.discountedPrice = discountedPrice;
                return history;
        }

        @Override
        public long getDiscountedPrice() {
                return discountedPrice;
        }

}
