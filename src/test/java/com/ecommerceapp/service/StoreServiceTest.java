// StoreService Tests
package com.ecommerceapp.service;

import com.ecommerceapp.dto.product.AddProductRequest;
import com.ecommerceapp.dto.store.StoreRequest;
import com.ecommerceapp.dto.store.StoreProductResponse;
import com.ecommerceapp.dto.store.StoreResponse;
import com.ecommerceapp.model.Product;
import com.ecommerceapp.model.Store;
import com.ecommerceapp.model.StoreProduct;
import com.ecommerceapp.repository.ProductRepository;
import com.ecommerceapp.repository.StoreProductRepository;
import com.ecommerceapp.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @Mock private StoreRepository storeRepository;
    @Mock private ProductRepository productRepository;
    @Mock private StoreProductRepository storeProductRepository;
    @InjectMocks private StoreService storeService;

    @Test
    void testCreateStore() {
        StoreRequest request = new StoreRequest();
        request.setName("Test Store");
        request.setDescription("Test Description");

        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        store.setDescription("Test Description");

        when(storeRepository.save(any(Store.class))).thenReturn(store);

        StoreResponse response = storeService.createStore(request);
        assertEquals("Test Store", response.getName());
        assertEquals(1L, response.getId());
    }

    @Test
    void testAddProductToStore() {
        AddProductRequest request = new AddProductRequest();
        request.setProductName("Test Product");
        request.setPrice(10.0);
        request.setQuantity(5);

        Store store = new Store();
        store.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(product));
        when(storeProductRepository.findByStoreIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        when(storeProductRepository.save(any(StoreProduct.class))).thenAnswer(i -> i.getArgument(0));

        StoreProductResponse response = storeService.addProductToStore(1L, request);
        assertEquals("Test Product", response.getProductName());
        assertEquals(10.0, response.getPrice());
    }
}
