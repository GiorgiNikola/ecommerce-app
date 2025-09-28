package com.ecommerceapp.service;

import com.ecommerceapp.dto.purchase.PurchaseRequest;
import com.ecommerceapp.dto.purchase.PurchaseResponse;
import com.ecommerceapp.model.*;
import com.ecommerceapp.repository.*;
import com.ecommerceapp.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private StoreRepository storeRepository; // ADDED
    @Mock private StoreProductRepository storeProductRepository;
    @Mock private UserPurchaseRepository userPurchaseRepository;
    @Mock private DailySalesReportRepository dailySalesReportRepository; // ADDED
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private UserService userService;

    @Test
    void testPurchaseProduct() {
        PurchaseRequest request = new PurchaseRequest();
        request.setStoreProductId(1L);
        request.setQuantity(2);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setId(1L);
        storeProduct.setStore(store);
        storeProduct.setProduct(product);
        storeProduct.setPrice(10.0);
        storeProduct.setQuantity(5);

        when(jwtUtil.extractUsername("token")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(storeProductRepository.findById(1L)).thenReturn(Optional.of(storeProduct));
        when(userPurchaseRepository.save(any(UserPurchase.class))).thenAnswer(i -> {
            UserPurchase purchase = i.getArgument(0);
            purchase.setId(1L);
            return purchase;
        });

        PurchaseResponse response = userService.purchaseProduct("Bearer token", request);
        assertEquals("Test Product", response.getProductName());
        assertEquals("Test Store", response.getStoreName());
        assertEquals(2, response.getQuantity());
        assertEquals(10.0, response.getPrice());
        assertEquals(20.0, response.getTotalPrice());
    }

    @Test
    void testGenerateDailySalesReports() {
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");

        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(dailySalesReportRepository.findByStoreIdAndReportDate(any(), any())).thenReturn(Optional.empty());
        when(userPurchaseRepository.findAll()).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> userService.generateDailySalesReports());
        verify(dailySalesReportRepository).save(any(DailySalesReport.class));
    }
}