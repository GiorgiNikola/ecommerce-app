package com.ecommerceapp.controller;

import com.ecommerceapp.dto.purchase.PurchaseRequest;
import com.ecommerceapp.dto.purchase.PurchaseResponse;
import com.ecommerceapp.dto.registration.UserResponse;
import com.ecommerceapp.dto.report.DailySalesReportResponse;
import com.ecommerceapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Users & Purchases", description = "User management and purchase endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;

    @PostMapping("/purchase")
    @Operation(summary = "Purchase product", description = "Purchase a product from store (USER & ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product purchased successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock or product not found")
    })
    public ResponseEntity<PurchaseResponse> purchaseProduct(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @Valid @RequestBody PurchaseRequest request) {
        PurchaseResponse response = userService.purchaseProduct(token, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-purchases")
    @Operation(summary = "Get my purchases", description = "Get current user's purchase history (USER & ADMIN)")
    public ResponseEntity<List<PurchaseResponse>> getMyPurchases(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserPurchases(token));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Get list of all users (ADMIN only)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/users/{userId}/purchases")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user's purchases", description = "Get specific user's purchase history (ADMIN only)")
    public ResponseEntity<List<PurchaseResponse>> getUserPurchases(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserPurchases(userId));
    }

    @PutMapping("/admin/users/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivate user account (ADMIN only)")
    @ApiResponse(responseCode = "200", description = "User deactivated successfully")
    public ResponseEntity<Map<String, String>> deactivateUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
    }

    @GetMapping("/admin/reports/daily/{storeId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get daily sales reports", description = "Get daily sales reports for a store (ADMIN only)")
    public ResponseEntity<List<DailySalesReportResponse>> getDailySalesReports(
            @Parameter(description = "Store ID") @PathVariable Long storeId) {
        return ResponseEntity.ok(userService.getDailySalesReports(storeId));
    }
}