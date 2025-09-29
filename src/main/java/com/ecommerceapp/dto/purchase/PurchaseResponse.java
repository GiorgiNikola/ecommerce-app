package com.ecommerceapp.dto.purchase;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter // ADDED THIS
public class PurchaseResponse {
    private Long id;
    private String productName;
    private String storeName;
    private int quantity;
    private double price;
    private double totalPrice;
    private LocalDateTime purchaseDate;
}