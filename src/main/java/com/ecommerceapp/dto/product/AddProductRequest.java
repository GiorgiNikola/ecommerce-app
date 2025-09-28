package com.ecommerceapp.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductRequest {
    @NotBlank
    private String productName;
    private String description;

    @Positive
    private double price;

    @Min(0)
    private int quantity;
}
