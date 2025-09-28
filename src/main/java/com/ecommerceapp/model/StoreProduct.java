package com.ecommerceapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_products")
@Getter
@Setter
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Store store;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    @Positive
    private double price;

    @Column(nullable = false)
    @Min(0)
    private int quantity;

    @OneToMany(mappedBy = "storeProduct")
    private List<UserPurchase> purchases = new ArrayList<>();
}
