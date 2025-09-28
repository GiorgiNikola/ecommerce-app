package com.ecommerceapp.repository;

import com.ecommerceapp.model.UserPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPurchaseRepository extends JpaRepository<UserPurchase, Long> {
    List<UserPurchase> findByUserId(Long userId);

    List<UserPurchase> findByStoreProductId(Long storeProductId);

    @Query("SELECT p FROM UserPurchase p WHERE p.user.id = :userId AND p.storeProduct.store.id = :storeId")
    List<UserPurchase> findPurchasesByUserAndStore(@Param("userId") Long userId, @Param("storeId") Long storeId);
}
