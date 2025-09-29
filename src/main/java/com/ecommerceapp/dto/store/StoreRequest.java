package com.ecommerceapp.dto.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    @NotBlank
    private String name;
    private String description;
}
