package com.ecommerceapp.dto.storeproduct;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StoreProductResponse {
    private Long id;
    private Long storeId;
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
}
