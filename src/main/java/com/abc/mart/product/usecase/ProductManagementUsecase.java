package com.abc.mart.product.usecase;

import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.product.usecase.dto.ResFetchStock;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductManagementUsecase {

    private final ProductRepository productRepository;

    public void productStatusManagement(String productId, boolean onSale){
        var product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.manageProductAvailability(onSale);
        productRepository.save(product);
    }

    @Transactional
    public List<ResFetchStock> fetchStock(List<String> productIds){
        var products = productRepository.findAllByProductIdAndIsAvailable(productIds, true);
        return products.stream().map(product -> new ResFetchStock(
                product.getId(),
                product.getName(),
                product.getStockCount())).toList();
    }

    @Transactional
    public void orderStock(String productId, int quantity){
        var product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.addStock(quantity);
        productRepository.save(product);
    }
}
