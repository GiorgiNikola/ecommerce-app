package com.ecommerceapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_purchases")
@Setter
@Getter
public class UserPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private StoreProduct storeProduct;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime purchaseDate = LocalDateTime.now();
}
