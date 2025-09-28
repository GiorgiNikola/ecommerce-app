package com.ecommerceapp.service;

import com.ecommerceapp.annotation.LogMethod;
import com.ecommerceapp.dto.product.AddProductRequest;
import com.ecommerceapp.dto.product.UpdateStoreProductRequest;
import com.ecommerceapp.dto.store.StoreRequest;
import com.ecommerceapp.dto.store.StoreProductResponse;
import com.ecommerceapp.dto.store.StoreResponse;
import com.ecommerceapp.model.Product;
import com.ecommerceapp.model.Store;
import com.ecommerceapp.model.StoreProduct;
import com.ecommerceapp.repository.ProductRepository;
import com.ecommerceapp.repository.StoreProductRepository;
import com.ecommerceapp.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;

    @LogMethod("Get all stores")
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::toStoreResponse)
                .collect(Collectors.toList());
    }

    @LogMethod("Get store by ID")
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return toStoreResponse(store);
    }

    @LogMethod("Get store products")
    public List<StoreProductResponse> getStoreProducts(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new RuntimeException("Store not found");
        }
        return storeProductRepository.findByStoreId(storeId).stream()
                .map(this::toStoreProductResponse)
                .collect(Collectors.toList());
    }

    @LogMethod("Get store product")
    public StoreProductResponse getStoreProduct(Long storeId, Long productId) {
        StoreProduct storeProduct = storeProductRepository.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new RuntimeException("Product not found in this store"));
        return toStoreProductResponse(storeProduct);
    }

    @LogMethod("Create store")
    public StoreResponse createStore(StoreRequest request) {
        Store store = new Store();
        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store = storeRepository.save(store);
        return toStoreResponse(store);
    }

    @LogMethod("Update store")
    public StoreResponse updateStore(Long id, StoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store = storeRepository.save(store);
        return toStoreResponse(store);
    }

    @LogMethod("Delete store")
    public void deleteStore(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new RuntimeException("Store not found");
        }
        storeRepository.deleteById(id);
    }

    @LogMethod("Add product to store")
    public StoreProductResponse addProductToStore(Long storeId, AddProductRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Create product if it doesn't exist
        Product product = productRepository.findByName(request.getProductName())
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setName(request.getProductName());
                    newProduct.setDescription(request.getDescription());
                    return productRepository.save(newProduct);
                });

        // Check if product already exists in this store
        if (storeProductRepository.findByStoreIdAndProductId(storeId, product.getId()).isPresent()) {
            throw new RuntimeException("Product already exists in this store");
        }

        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setStore(store);
        storeProduct.setProduct(product);
        storeProduct.setPrice(request.getPrice());
        storeProduct.setQuantity(request.getQuantity());
        storeProduct = storeProductRepository.save(storeProduct);

        return toStoreProductResponse(storeProduct);
    }

    @LogMethod("Update store product")
    public StoreProductResponse updateStoreProduct(Long storeId, Long productId, UpdateStoreProductRequest request) {
        StoreProduct storeProduct = storeProductRepository.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new RuntimeException("Product not found in this store"));

        storeProduct.setPrice(request.getPrice());
        storeProduct.setQuantity(storeProduct.getQuantity() + request.getQuantityToAdd());
        storeProduct = storeProductRepository.save(storeProduct);

        return toStoreProductResponse(storeProduct);
    }

    @LogMethod("Remove product from store")
    public void removeProductFromStore(Long storeId, Long productId) {
        StoreProduct storeProduct = storeProductRepository.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new RuntimeException("Product not found in this store"));
        storeProductRepository.delete(storeProduct);
    }

    private StoreResponse toStoreResponse(Store store) {
        StoreResponse response = new StoreResponse();
        response.setId(store.getId());
        response.setName(store.getName());
        response.setDescription(store.getDescription());
        return response;
    }

    private StoreProductResponse toStoreProductResponse(StoreProduct sp) {
        StoreProductResponse response = new StoreProductResponse();
        response.setId(sp.getId());
        response.setProductId(sp.getProduct().getId());
        response.setProductName(sp.getProduct().getName());
        response.setDescription(sp.getProduct().getDescription());
        response.setPrice(sp.getPrice());
        response.setQuantity(sp.getQuantity());
        return response;
    }
}