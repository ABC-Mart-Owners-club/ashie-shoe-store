package com.abc.mart.product.domain.repository;

import com.abc.mart.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    List<Product> findAllByProductIdAndIsAvailable(List<String> productId, boolean isAvailable);

    void save(List<Product> products);
    void save(Product product);

    Optional<Product> findByProductId(String productId);
    Optional<Product> findByProductIdAndIsAvailable(String productId, boolean isAvailable);
}
