package com.ecommerceapp.dto.product;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStoreProductRequest {
    @Positive
    private double price;

    private int quantityToAdd; // Can be negative to reduce stock
}
