package com.ecommerceapp.dto.purchase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {
    @NotNull
    private Long storeProductId;

    @Min(1)
    private int quantity;
}
