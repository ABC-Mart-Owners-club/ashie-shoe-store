package com.abc.mart.order.domain.repository;

import com.abc.mart.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    List<Product> findByProductId(List<String> productId);
}
