package com.abc.mart.order.usecase;

import com.abc.mart.order.domain.Order;
import com.abc.mart.order.domain.OrderId;
import com.abc.mart.order.domain.repository.OrderRepository;
import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.order.usecase.dto.PartialOrderCancelRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartialCancelOrderUsecase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order cancelPartialOrder(PartialOrderCancelRequest request) {
        var order = orderRepository.findById(OrderId.of(request.orderId()));

        var cancelledItems = order.partialCancelOrder(request.cancelProductIds());

        cancelledItems.forEach(item -> productRepository.findByProductIdAndIsAvailable(item.getProductId(), true)
                .ifPresentOrElse(
                p -> {
                    var before = p.getStocks();
                    p.restoreStock(item.getStockIds());
                    log.info("Product {} stock updated: {} -> {}", p.getId(), before, p.getStocks());
                    productRepository.save(p);
                },
                () ->
                    log.error("Product not on sale: {}", item.getProductId())
                    //따로 예외로 터뜨리진 않음 (해당 상품이 삭제되었거나 할 수 있기 때문에 에러 메세지로 처리)
        ));

        return order;
    }
}
