package com.petshop.controller;

import com.petshop.dto.AddCartItemRequest;
import com.petshop.model.Cart;
import com.petshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts/{sessionId}")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart get(@PathVariable String sessionId) {
        return cartService.getOrCreate(sessionId);
    }

    @PostMapping("/items")
    public Cart addItem(@PathVariable String sessionId, @Valid @RequestBody AddCartItemRequest request) {
        return cartService.addItem(sessionId, request);
    }

    @PatchMapping("/items/{productId}")
    public Cart updateQuantity(
            @PathVariable String sessionId,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        return cartService.updateQuantity(sessionId, productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public Cart removeItem(@PathVariable String sessionId, @PathVariable Long productId) {
        return cartService.removeItem(sessionId, productId);
    }
}
