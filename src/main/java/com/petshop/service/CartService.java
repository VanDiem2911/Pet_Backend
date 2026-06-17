package com.petshop.service;

import com.petshop.dto.AddCartItemRequest;
import com.petshop.model.Cart;
import com.petshop.model.CartItem;
import com.petshop.model.Product;
import com.petshop.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart getOrCreate(String sessionId) {
        return cartRepository.findBySessionId(sessionId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setSessionId(sessionId);
            return cartRepository.save(cart);
        });
    }

    public Cart addItem(String sessionId, AddCartItemRequest request) {
        Product product = productService.getByProductId(request.productId());
        Cart cart = getOrCreate(sessionId);
        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getProductId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setProductId(product.getId());
                    newItem.setName(product.getName());
                    newItem.setBrand(product.getBrand());
                    newItem.setPrice(product.getPrice());
                    newItem.setEmoji(product.getEmoji());
                    newItem.setImageUrl(product.getImageUrl());
                    cart.getItems().add(newItem);
                    return newItem;
                });
        item.setQuantity(item.getQuantity() + request.quantity());
        return cartRepository.save(cart);
    }

    public Cart updateQuantity(String sessionId, Long productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        Cart cart = getOrCreate(sessionId);
        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product is not in cart"))
                .setQuantity(quantity);
        return cartRepository.save(cart);
    }

    public Cart removeItem(String sessionId, Long productId) {
        Cart cart = getOrCreate(sessionId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    public void clear(String sessionId) {
        Cart cart = getOrCreate(sessionId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
