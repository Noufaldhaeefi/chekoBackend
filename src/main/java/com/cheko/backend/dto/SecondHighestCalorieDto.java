package com.cheko.backend.dto;

import java.math.BigDecimal;

public class SecondHighestCalorieDto {
    private Long itemId;
    private String itemName;
    private String description;
    private Integer calories;
    private BigDecimal price;
    private String imageUrl;
    private String categoryName;
    private Boolean isAvailable;
    private Boolean isBestSeller;

    // Constructors
    public SecondHighestCalorieDto() {}

    public SecondHighestCalorieDto(Long itemId, String itemName, String description, Integer calories, 
                                  BigDecimal price, String imageUrl, String categoryName, 
                                  Boolean isAvailable, Boolean isBestSeller) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.isAvailable = isAvailable;
        this.isBestSeller = isBestSeller;
    }

    // Builder pattern for easier construction
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long itemId;
        private String itemName;
        private String description;
        private Integer calories;
        private BigDecimal price;
        private String imageUrl;
        private String categoryName;
        private Boolean isAvailable;
        private Boolean isBestSeller;

        public Builder itemId(Long itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder calories(Integer calories) {
            this.calories = calories;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder isAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public Builder isBestSeller(Boolean isBestSeller) {
            this.isBestSeller = isBestSeller;
            return this;
        }

        public SecondHighestCalorieDto build() {
            return new SecondHighestCalorieDto(itemId, itemName, description, calories, 
                                              price, imageUrl, categoryName, isAvailable, isBestSeller);
        }
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Boolean getIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(Boolean isBestSeller) {
        this.isBestSeller = isBestSeller;
    }
}

