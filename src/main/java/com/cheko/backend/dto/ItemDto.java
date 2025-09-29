package com.cheko.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer calories;
    private String imageUrl;
    private Long categoryId;
    private String categoryName; // Include category name for convenience
    private Boolean isAvailable;
    private Integer totalOrders;
    private Boolean isBestSeller;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ItemDto() {}

    public ItemDto(Long id, String name, String description, BigDecimal price, Integer calories, 
                  String imageUrl, Long categoryId, String categoryName, Boolean isAvailable, 
                  Integer totalOrders, Boolean isBestSeller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isAvailable = isAvailable;
        this.totalOrders = totalOrders;
        this.isBestSeller = isBestSeller;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Boolean getIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(Boolean isBestSeller) {
        this.isBestSeller = isBestSeller;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

