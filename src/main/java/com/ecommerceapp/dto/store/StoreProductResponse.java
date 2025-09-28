package com.ecommerceapp.dto.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreProductResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;
}