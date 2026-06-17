package com.petshop.controller;

import com.petshop.model.Appointment;
import com.petshop.model.Order;
import com.petshop.model.Product;
import com.petshop.repository.AppointmentRepository;
import com.petshop.repository.OrderRepository;
import com.petshop.repository.ProductRepository;
import com.petshop.service.AppointmentService;
import com.petshop.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final String adminUser;
    private final String adminPass;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;
    private final OrderRepository orderRepository;

    // Simple in-memory token store (sufficient for single-admin use)
    private String currentToken = null;

    public AdminController(
            @Value("${app.admin.username:admin}") String adminUser,
            @Value("${app.admin.password:petshop@2024}") String adminPass,
            ProductRepository productRepository,
            ProductService productService,
            AppointmentRepository appointmentRepository,
            AppointmentService appointmentService,
            OrderRepository orderRepository
    ) {
        this.adminUser = adminUser;
        this.adminPass = adminPass;
        this.productRepository = productRepository;
        this.productService = productService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
        this.orderRepository = orderRepository;
    }

    // ── AUTH ──────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String u = body.getOrDefault("username", "");
        String p = body.getOrDefault("password", "");
        if (!adminUser.equals(u) || !adminPass.equals(p)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu");
        }
        currentToken = UUID.randomUUID().toString();
        return Map.of("token", currentToken);
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        currentToken = null;
        return Map.of("message", "Đã đăng xuất");
    }

    // ── GUARD ─────────────────────────────────────────────────────────────

    private void requireAuth(String token) {
        if (token == null || !token.equals(currentToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }
    }

    // ── STATS ─────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public Map<String, Object> stats(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAuth(token);
        long totalProducts  = productRepository.count();
        long totalOrders    = orderRepository.count();
        long totalAppointments = appointmentRepository.count();
        long pendingAppointments = appointmentRepository.findAll().stream()
                .filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
        return Map.of(
                "totalProducts", totalProducts,
                "totalOrders", totalOrders,
                "totalAppointments", totalAppointments,
                "pendingAppointments", pendingAppointments
        );
    }

    // ── APPOINTMENTS ──────────────────────────────────────────────────────

    @GetMapping("/appointments")
    public List<Appointment> appointments(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAuth(token);
        return appointmentRepository.findAll();
    }

    @PatchMapping("/appointments/{id}/status")
    public Appointment updateAppointmentStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        requireAuth(token);
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lịch hẹn"));
        a.setStatus(body.getOrDefault("status", a.getStatus()));
        return appointmentRepository.save(a);
    }

    @DeleteMapping("/appointments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(
            @PathVariable String id,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        requireAuth(token);
        appointmentRepository.deleteById(id);
    }

    // ── PRODUCTS ──────────────────────────────────────────────────────────

    @GetMapping("/products")
    public List<Product> products(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAuth(token);
        return productRepository.findAll();
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(
            @Valid @RequestBody Product product,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        requireAuth(token);
        return productService.save(product);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        requireAuth(token);
        return productService.update(id, product);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        requireAuth(token);
        productService.delete(id);
    }

    // ── ORDERS ────────────────────────────────────────────────────────────

    @GetMapping("/orders")
    public List<Order> orders(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        requireAuth(token);
        return orderRepository.findAll();
    }

    @PatchMapping("/orders/{id}/status")
    public Order updateOrderStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        requireAuth(token);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));
        order.setStatus(body.getOrDefault("status", order.getStatus()));
        return orderRepository.save(order);
    }
}
