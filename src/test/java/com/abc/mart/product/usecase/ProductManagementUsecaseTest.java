package com.abc.mart.product.usecase;

import com.abc.mart.product.Stock;
import com.abc.mart.product.domain.Product;
import com.abc.mart.product.domain.repository.ProductRepository;
import com.abc.mart.product.usecase.dto.ReqStock;
import com.abc.mart.product.usecase.dto.ResFetchStock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.abc.mart.test.TestStubCreator.generateRandomStocks;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductManagementUsecaseTest {

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    ProductManagementUsecase usecase = new ProductManagementUsecase(productRepository);

    @Test
    void testProductStatusManagement() {
        // given
        var productId = "productId1";
        var product = Product.of(productId, "productName", 100, generateRandomStocks(10), true);
        when(productRepository.findByProductId(productId)).thenReturn(Optional.of(product));

        // when
        usecase.productStatusManagement(productId, false);

        // then
        verify(productRepository).save(product);
        assertFalse(product.isAvaliable());
    }

    @Test
    void testFetchStock() {
        // given
        var productId1 = "productId1";
        var productId2 = "productId2";

        var product1 = Product.of(productId1, "productName1", 100,
                generateRandomStocks(3), true);

        var product2 = Product.of(productId2, "productName2", 200,
                generateRandomStocks(2), true);

        when(productRepository.findAllByProductIdAndIsAvailable(List.of(productId1, productId2), true))
                .thenReturn(List.of(product1, product2));

        // when
        var result = usecase.fetchStock(List.of(productId1, productId2));

        // then
        assertEquals(2, result.size());
        assertEquals(new ResFetchStock(productId1, "productName1", 3), result.get(0));
        assertEquals(new ResFetchStock(productId2, "productName2", 2), result.get(1));
    }

    @Test
    void testOrderStock() {
        // given
        var productId = "productId1";
        var product = Product.of(productId, "productName", 100,
                generateRandomStocks(3), true);
        when(productRepository.findByProductId(productId)).thenReturn(Optional.of(product));

        // when
        usecase.orderStock(productId, new ArrayList<>(Arrays.asList(new ReqStock("1",LocalDateTime.of(2023, 10, 1, 0, 0, 1, 456)),
                new ReqStock("2", LocalDateTime.of(2023, 10, 1, 0, 0,2, 234)))));


        // then
        verify(productRepository).save(product);
        assertEquals(5, product.getStocks().size());
    }



    @Test
    void testProductNotFound() {
        // given
        var productId = "invalidProductId";
        when(productRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> usecase.productStatusManagement(productId, true));
        assertThrows(IllegalArgumentException.class, () -> usecase.orderStock(productId,
                List.of(new ReqStock("1", LocalDateTime.of(2023, 10, 1, 0, 0, 1, 456)))));
    }
}