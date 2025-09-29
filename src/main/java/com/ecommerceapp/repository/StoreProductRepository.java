package com.ecommerceapp.repository;

import com.ecommerceapp.model.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    List<StoreProduct> findByStoreId(Long storeId);

    List<StoreProduct> findByProductId(Long productId);

    Optional<StoreProduct> findByStoreIdAndProductId(Long storeId, Long productId);
}
