package com.abc.mart.order.usecase;

import com.abc.mart.common.annotation.RedissonLock;
import com.abc.mart.coupon.domain.CouponRepository;
import com.abc.mart.coupon.service.CouponService;
import com.abc.mart.member.domain.repository.MemberRepository;
import com.abc.mart.order.domain.*;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.product.Stock;
import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceOrderUsecase {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final CouponService couponService;

    @Transactional
    public Pair<Order, Map<String, Product>> placeOrder(OrderRequest orderRequest) {

        var customer = Customer.from(memberRepository.findByMemberId(orderRequest.orderMemberId()));

        var productIds = orderRequest.orderItemRequestList().stream()
                .map(OrderRequest.OrderItemRequest::productId).toList();

        var productMap = productRepository.findAllByProductIdAndIsAvailable(productIds, true).stream().collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderItem> orderItems = new ArrayList<>();

        List<CouponRedemptionHistory> couponRedemptionHistories = new ArrayList<>();

        for(OrderRequest.OrderItemRequest orderItemRequest : orderRequest.orderItemRequestList()) {
            var product = productMap.get(orderItemRequest.productId());

            if (product == null) {
                //찾을 수 없는 상품은 주문단계에서 예외처리.
                throw new IllegalArgumentException("Product not found: " + orderItemRequest.productId());
            }

            var quantity = orderItemRequest.quantity();

            var orderItem = OrderItem.of(product, product.getPrice(), quantity);

            var orderedProduct = productMap.get(orderItem.getProductId());
            var soldStocks = orderedProduct.subtractStock(orderItem.getQuantity());

            //if stock coupon is available, apply it
            var stockDiscountAmount = couponService.applyStockCouponAndReturnDiscountedAmount(customer.getMemberId(),
                    soldStocks, orderedProduct.getPrice());
            orderItem.setStockIds(soldStocks.stream().map(Stock::getId).toList());
            if(stockDiscountAmount > 0) {
                couponRedemptionHistories.add(StockCouponRedemptionHistory.create(orderItem.getId(), stockDiscountAmount));
            }

            productRepository.save(orderedProduct);

            orderItems.add(orderItem);
        }



        var order = Order.createOrder(orderItems, customer);

        //if seller coupon is available, apply it
        var beforeUniversalCouponAppliedPrice = orderItems.stream().mapToLong(OrderItem::getTotalPrice).sum();
        var applyUniversalCouponDiscountedAmount = couponService.applyUniversalCouponAndReturnDiscountedAmount(customer.getMemberId(), beforeUniversalCouponAppliedPrice);

        if (applyUniversalCouponDiscountedAmount > 0) {
            couponRedemptionHistories.add(UniversalCouponRedemptionHistory.create(applyUniversalCouponDiscountedAmount, order.getOrderId().id()));
        }

        order.saveCouponRedemptionHistories(couponRedemptionHistories); // Save coupon redemption histories to the order
        orderRepository.placeOrder(order);

        return Pair.of(order, productMap);
    }

}
