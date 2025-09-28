package com.ecommerceapp.service;

import com.ecommerceapp.annotation.LogMethod;
import com.ecommerceapp.dto.purchase.PurchaseRequest;
import com.ecommerceapp.dto.purchase.PurchaseResponse;
import com.ecommerceapp.dto.registration.UserResponse;
import com.ecommerceapp.dto.report.DailySalesReportResponse;
import com.ecommerceapp.model.*;
import com.ecommerceapp.repository.*;
import com.ecommerceapp.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository; // ADDED THIS
    private final StoreProductRepository storeProductRepository;
    private final UserPurchaseRepository userPurchaseRepository;
    private final DailySalesReportRepository dailySalesReportRepository;
    private final JwtUtil jwtUtil;

    @LogMethod("Purchase product")
    @Transactional
    public PurchaseResponse purchaseProduct(String token, PurchaseRequest request) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StoreProduct storeProduct = storeProductRepository.findById(request.getStoreProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (storeProduct.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        // Update inventory
        storeProduct.setQuantity(storeProduct.getQuantity() - request.getQuantity());
        storeProductRepository.save(storeProduct);

        // Create purchase record
        UserPurchase purchase = new UserPurchase();
        purchase.setUser(user);
        purchase.setStoreProduct(storeProduct);
        purchase.setQuantity(request.getQuantity());
        purchase = userPurchaseRepository.save(purchase);

        return toPurchaseResponse(purchase);
    }

    @LogMethod("Get user purchases by token")
    public List<PurchaseResponse> getUserPurchases(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userPurchaseRepository.findByUserId(user.getId()).stream()
                .map(this::toPurchaseResponse)
                .collect(Collectors.toList());
    }

    @LogMethod("Get all users")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @LogMethod("Get user by ID")
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toUserResponse(user);
    }

    @LogMethod("Get user purchases by ID")
    public List<PurchaseResponse> getUserPurchases(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return userPurchaseRepository.findByUserId(userId).stream()
                .map(this::toPurchaseResponse)
                .collect(Collectors.toList());
    }

    @LogMethod("Deactivate user")
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @LogMethod("Activate user")
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    @LogMethod("Get daily sales reports")
    public List<DailySalesReportResponse> getDailySalesReports(Long storeId) {
        return dailySalesReportRepository.findByStoreId(storeId).stream()
                .map(this::toReportResponse)
                .collect(Collectors.toList());
    }

    // Scheduler for daily reports
    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    @LogMethod("Generate daily sales reports")
    @Transactional
    public void generateDailySalesReports() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.plusDays(1).atStartOfDay().minusSeconds(1);

        // Get all stores
        List<Store> stores = storeRepository.findAll();

        for (Store store : stores) {
            // Check if report already exists
            if (dailySalesReportRepository.findByStoreIdAndReportDate(store.getId(), yesterday).isPresent()) {
                continue;
            }

            // Calculate total sales for this store
            double totalSales = userPurchaseRepository.findAll().stream()
                    .filter(p -> p.getStoreProduct().getStore().getId().equals(store.getId()))
                    .filter(p -> !p.getPurchaseDate().isBefore(startOfDay) && !p.getPurchaseDate().isAfter(endOfDay))
                    .mapToDouble(p -> p.getQuantity() * p.getStoreProduct().getPrice())
                    .sum();

            // Save report
            DailySalesReport report = new DailySalesReport();
            report.setStore(store);
            report.setReportDate(yesterday);
            report.setTotalSales(totalSales);
            dailySalesReportRepository.save(report);
        }
    }

    private PurchaseResponse toPurchaseResponse(UserPurchase purchase) {
        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setProductName(purchase.getStoreProduct().getProduct().getName());
        response.setStoreName(purchase.getStoreProduct().getStore().getName());
        response.setQuantity(purchase.getQuantity());
        response.setPrice(purchase.getStoreProduct().getPrice());
        response.setTotalPrice(purchase.getQuantity() * purchase.getStoreProduct().getPrice());
        response.setPurchaseDate(purchase.getPurchaseDate());
        return response;
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setActive(user.isActive());
        return response;
    }

    private DailySalesReportResponse toReportResponse(DailySalesReport report) {
        DailySalesReportResponse response = new DailySalesReportResponse();
        response.setId(report.getId());
        response.setStoreId(report.getStore().getId());
        response.setStoreName(report.getStore().getName());
        response.setReportDate(report.getReportDate());
        response.setTotalSales(report.getTotalSales());
        return response;
    }
}