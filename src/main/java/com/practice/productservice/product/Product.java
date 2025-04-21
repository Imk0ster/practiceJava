package com.practice.productservice.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
        int id,
        String name,
        String description,
        BigDecimal price,
        String category,
        Integer stockQuantity,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isActive
) {
    public Product {
        if (name != null) {
            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            if (name.length() > 255) {
                throw new IllegalArgumentException("Name must not exceed 255 characters");
            }
        }
        if (description != null && description.length() > 255) {
            throw new IllegalArgumentException("Description must not exceed 255 characters");
        }
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (price.scale() > 2 || price.precision() > 10) {
                throw new IllegalArgumentException("Price must have at most 8 integer digits and 2 decimal places");
            }
        }
        if (category != null) {
            if (category.trim().isEmpty()) {
                throw new IllegalArgumentException("Category must not be empty");
            }
            if (category.length() > 100) {
                throw new IllegalArgumentException("Category must not exceed 100 characters");
            }
        }
        if (stockQuantity != null && stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity must not be negative");
        }
        if (imageUrl != null && imageUrl.length() > 255) {
            throw new IllegalArgumentException("Image URL must not exceed 255 characters");
        }
//        if (createdAt == null) {
//            throw new IllegalArgumentException("Created date must not be null");
//        }
//        if (updatedAt == null) {
//            throw new IllegalArgumentException("Updated date must not be null");
//        }
    }
}