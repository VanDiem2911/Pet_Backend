package com.petshop.controller;

import com.petshop.dto.CreateOrderRequest;
import com.petshop.model.Order;
import com.petshop.repository.OrderRepository;
import com.petshop.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Comparator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @GetMapping("/track")
    public List<Order> trackByPhone(@RequestParam String phone) {
        String normalizedPhone = normalizePhone(phone);
        if (normalizedPhone.isBlank()) {
            return List.of();
        }

        return orderRepository.findAll().stream()
                .filter(order -> normalizePhone(order.getPhone()).equals(normalizedPhone))
                .sorted(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    private String normalizePhone(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[^0-9]", "");
    }
}
