package com.ecommerceapp.controller;

import com.ecommerceapp.dto.product.AddProductRequest;
import com.ecommerceapp.dto.product.UpdateStoreProductRequest;
import com.ecommerceapp.dto.store.StoreRequest;
import com.ecommerceapp.dto.store.StoreResponse;
import com.ecommerceapp.dto.store.StoreProductResponse;
import com.ecommerceapp.service.StoreService;
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

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "Stores", description = "Store and product management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    @Operation(summary = "Get all stores", description = "Get list of all stores (USER & ADMIN)")
    @ApiResponse(responseCode = "200", description = "List of stores returned successfully")
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get store by ID", description = "Get specific store details (USER & ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store found"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    public ResponseEntity<StoreResponse> getStoreById(
            @Parameter(description = "Store ID") @PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping("/{storeId}/products")
    @Operation(summary = "Get products in store", description = "Get all products available in a specific store (USER & ADMIN)")
    public ResponseEntity<List<StoreProductResponse>> getStoreProducts(
            @Parameter(description = "Store ID") @PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreProducts(storeId));
    }

    @GetMapping("/{storeId}/products/{productId}")
    @Operation(summary = "Get product details in store", description = "Get specific product details including price and quantity (USER & ADMIN)")
    public ResponseEntity<StoreProductResponse> getStoreProduct(
            @Parameter(description = "Store ID") @PathVariable Long storeId,
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        return ResponseEntity.ok(storeService.getStoreProduct(storeId, productId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new store", description = "Create a new store (ADMIN only)")
    @ApiResponse(responseCode = "201", description = "Store created successfully")
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody StoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update store", description = "Update store information (ADMIN only)")
    public ResponseEntity<StoreResponse> updateStore(
            @Parameter(description = "Store ID") @PathVariable Long id,
            @Valid @RequestBody StoreRequest request) {
        return ResponseEntity.ok(storeService.updateStore(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete store", description = "Delete a store (ADMIN only)")
    @ApiResponse(responseCode = "204", description = "Store deleted successfully")
    public ResponseEntity<Void> deleteStore(
            @Parameter(description = "Store ID") @PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{storeId}/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add product to store", description = "Add a product to store with price and quantity (ADMIN only)")
    @ApiResponse(responseCode = "201", description = "Product added to store successfully")
    public ResponseEntity<StoreProductResponse> addProductToStore(
            @Parameter(description = "Store ID") @PathVariable Long storeId,
            @Valid @RequestBody AddProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.addProductToStore(storeId, request));
    }

    @PutMapping("/{storeId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product in store", description = "Update product price and add/remove stock (ADMIN only)")
    public ResponseEntity<StoreProductResponse> updateStoreProduct(
            @Parameter(description = "Store ID") @PathVariable Long storeId,
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Valid @RequestBody UpdateStoreProductRequest request) {
        return ResponseEntity.ok(storeService.updateStoreProduct(storeId, productId, request));
    }

    @DeleteMapping("/{storeId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove product from store", description = "Remove product from store (ADMIN only)")
    @ApiResponse(responseCode = "204", description = "Product removed from store successfully")
    public ResponseEntity<Void> removeProductFromStore(
            @Parameter(description = "Store ID") @PathVariable Long storeId,
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        storeService.removeProductFromStore(storeId, productId);
        return ResponseEntity.noContent().build();
    }
}