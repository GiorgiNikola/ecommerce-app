package com.ecommerceapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
@Setter
@Getter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Store name is required")
    @Size(min = 3, max = 50, message = "Store name must be between 2 and 100 characters")
    private String name;

    @Column(length = 255)
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @OneToMany(mappedBy = "store")
    private List<StoreProduct> storeProducts = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<DailySalesReport> salesReports = new ArrayList<>();
}
