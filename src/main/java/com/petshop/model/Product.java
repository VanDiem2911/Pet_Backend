package com.petshop.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class Product {
    @Id
    private String mongoId;

    @Indexed(unique = true)
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @Min(0)
    private BigDecimal oldPrice;

    @Min(0)
    private int discount;

    private boolean hot;

    @Min(0)
    private int rating = 5;

    @Min(0)
    private int reviews;

    private String emoji;
    private String imageUrl;
    private String category;
    private Set<String> sections = new HashSet<>();
    private int stock = 100;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public String getMongoId() { return mongoId; }
    public void setMongoId(String mongoId) { this.mongoId = mongoId; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getOldPrice() { return oldPrice; }
    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }
    public int getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }
    public boolean isHot() { return hot; }
    public void setHot(boolean hot) { this.hot = hot; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public int getReviews() { return reviews; }
    public void setReviews(int reviews) { this.reviews = reviews; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Set<String> getSections() { return sections; }
    public void setSections(Set<String> sections) { this.sections = sections; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
