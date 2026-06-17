package com.petshop.service;

import com.petshop.dto.CreateOrderRequest;
import com.petshop.model.CartItem;
import com.petshop.model.Order;
import com.petshop.model.Product;
import com.petshop.repository.OrderRepository;
import com.petshop.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    public Order create(CreateOrderRequest request) {
        List<CartItem> items = request.items();
        if ((items == null || items.isEmpty()) && StringUtils.hasText(request.sessionId())) {
            items = cartService.getOrCreate(request.sessionId()).getItems();
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        List<CartItem> orderItems = reserveStockAndBuildOrderItems(items);

        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setPhone(request.phone());
        order.setAddress(request.address());
        order.setItems(orderItems);
        order.setTotal(orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order saved = orderRepository.save(order);
        if (StringUtils.hasText(request.sessionId())) {
            cartService.clear(request.sessionId());
        }
        return saved;
    }

    private List<CartItem> reserveStockAndBuildOrderItems(List<CartItem> items) {
        Map<Long, Integer> requestedQuantities = items.stream()
                .filter(item -> item.getProductId() != null)
                .collect(Collectors.toMap(
                        CartItem::getProductId,
                        CartItem::getQuantity,
                        Integer::sum
                ));

        if (requestedQuantities.isEmpty()) {
            throw new IllegalArgumentException("Order items must include product ids");
        }

        List<Product> productsToSave = new ArrayList<>();
        List<CartItem> orderItems = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            Long productId = entry.getKey();
            int quantity = entry.getValue();
            if (quantity < 1) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product " + productId + " not found"));

            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Not enough stock for " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productsToSave.add(product);

            CartItem snapshot = new CartItem();
            snapshot.setProductId(product.getId());
            snapshot.setName(product.getName());
            snapshot.setBrand(product.getBrand());
            snapshot.setPrice(product.getPrice());
            snapshot.setEmoji(product.getEmoji());
            snapshot.setImageUrl(product.getImageUrl());
            snapshot.setQuantity(quantity);
            orderItems.add(snapshot);
        }

        productRepository.saveAll(productsToSave);
        return orderItems;
    }
}
