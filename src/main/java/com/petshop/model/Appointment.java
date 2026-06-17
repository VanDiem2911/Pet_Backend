package com.petshop.model;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("appointments")
public class Appointment {
    @Id
    private String id;

    @NotBlank
    private String serviceType; // "grooming" | "boarding"

    @NotBlank
    private String customerName;

    @NotBlank
    private String phone;

    @NotBlank
    private String date; // "YYYY-MM-DD"

    private String checkoutDate; // boarding only
    private String timeSlot;    // grooming only
    private String notes;
    private String status = "PENDING";

    @CreatedDate
    private Instant createdAt;

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(String checkoutDate) { this.checkoutDate = checkoutDate; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
