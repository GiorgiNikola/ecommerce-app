package com.ecommerceapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "store_sales_reports")
@Setter
@Getter
public class DailySalesReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Store store;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(nullable = false)
    private double totalSales;
}
