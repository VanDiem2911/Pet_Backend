package com.petshop.dto;

import com.petshop.model.CartItem;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateOrderRequest(
        @NotBlank String customerName,
        @Email @NotBlank String email,
        @NotBlank String phone,
        @NotBlank String address,
        String sessionId,
        List<CartItem> items
) {
}
