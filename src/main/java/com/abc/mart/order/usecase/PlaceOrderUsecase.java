package com.abc.mart.order.usecase;

import com.abc.mart.member.domain.repository.MemberRepository;
import com.abc.mart.order.domain.Customer;
import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderItem;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.order.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.OrderRequest;
import com.abc.mart.product.domain.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceOrderUsecase {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {

        var customer = Customer.from(memberRepository.findByMemberId(orderRequest.orderMemberId()));

        var productIds = orderRequest.orderItemRequestList().stream()
                .map(OrderRequest.OrderItemRequest::productId).toList();

        var productMap = productRepository.findByProductId(productIds).stream().collect(Collectors.toMap(Product::getId, p -> p));

        var orderItems = orderRequest.orderItemRequestList().stream().map(orderItemRequest -> {
            var product = productMap.get(orderItemRequest.productId());
            var quantity = orderItemRequest.quantity();
            return OrderItem.of(product, quantity);
        }).toList();

        var order = Order.createOrder(orderItems, customer);

        orderRepository.placeOrder(order);

        return order;
    }

}
