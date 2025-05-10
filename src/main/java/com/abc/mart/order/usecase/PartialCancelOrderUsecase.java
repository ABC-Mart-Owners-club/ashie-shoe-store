package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartialCancelOrderUsecase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order cancelPartialOrder(PartialOrderCancelRequest request) {
        var order = orderRepository.findById(OrderId.of(request.orderId()));

        var canceledItems = order.partialCancelOrder(request.cancelProductIds());

        canceledItems.forEach(item -> productRepository.findByProductIdAndIsAvailable(item.getProductId(), true)
                .ifPresentOrElse(
                p -> {
                    var before = p.getStockCount();
                    p.addStock(item.getQuantity());
                    log.info("Product {} stock updated: {} -> {}", p.getId(), before, p.getStockCount());
                    productRepository.save(p);
                },
                () ->
                    log.error("Product not on sale: {}", item.getProductId())
                    //따로 예외로 터뜨리진 않음 (해당 상품이 삭제되었거나 할 수 있기 때문에 에러 메세지로 처리)
        ));

        return order;
    }
}
