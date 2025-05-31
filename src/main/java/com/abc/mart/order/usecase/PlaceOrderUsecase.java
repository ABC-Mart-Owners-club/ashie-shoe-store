package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.repository.MemberRepository;
import com.abc.mart.order.domain.Customer;
import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderItem;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceOrderUsecase {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Pair<Order, Map<String, Product>> placeOrder(OrderRequest orderRequest) {

        var customer = Customer.from(memberRepository.findByMemberId(orderRequest.orderMemberId()));

        var productIds = orderRequest.orderItemRequestList().stream()
                .map(OrderRequest.OrderItemRequest::productId).toList();

        var productMap = productRepository.findAllByProductIdAndIsAvailable(productIds, true).stream().collect(Collectors.toMap(Product::getId, p -> p));

        var orderItems = orderRequest.orderItemRequestList().stream().map(orderItemRequest -> {
            var product = productMap.get(orderItemRequest.productId());

            if (product == null) {
                //찾을 수 없는 상품은 주문단계에서 예외처리.
                throw new IllegalArgumentException("Product not found: " + orderItemRequest.productId());
            }

            var quantity = orderItemRequest.quantity();
            return OrderItem.of(product, quantity);
        }).toList();

        var order = Order.createOrder(orderItems, customer);

        orderRepository.placeOrder(order);

        orderItems.forEach(orderItem ->
        {
            var orderedProduct = productMap.get(orderItem.getProductId());
            orderedProduct.subtractStock(orderItem.getQuantity());
            productRepository.save(orderedProduct);
        });
        return Pair.of(order, productMap);
    }

}
