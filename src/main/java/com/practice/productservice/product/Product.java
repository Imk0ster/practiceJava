package com.practice.productservice.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Represents a product in the system")
public record Product(
        @Schema(description = "Unique identifier of the product", example = "1")
        int id,

        @Schema(description = "Name of the product", example = "Laptop", required = true)
        String name,

        @Schema(description = "Description of the product", example = "High-performance laptop")
        String description,

        @Schema(description = "Price of the product", example = "999.99")
        BigDecimal price,

        @Schema(description = "Category of the product", example = "Electronics")
        String category,

        @Schema(description = "Available stock quantity", example = "10")
        Integer stockQuantity,

        @Schema(description = "URL of the product image", example = "http://example.com/image.jpg")
        String imageUrl,

        @Schema(description = "Creation timestamp", example = "2025-04-01T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2025-04-01T10:00:00")
        LocalDateTime updatedAt,

        @Schema(description = "Whether the product is active", example = "true")
        Boolean isActive
) {
//    public Product {
//        if (name != null) {
//            if (name.trim().isEmpty()) {
//                throw new IllegalArgumentException("Name must not be empty");
//            }
//            if (name.length() > 255) {
//                throw new IllegalArgumentException("Name must not exceed 255 characters");
//            }
//        }
//        if (description != null && description.length() > 255) {
//            throw new IllegalArgumentException("Description must not exceed 255 characters");
//        }
//        if (price != null) {
//            if (price.compareTo(BigDecimal.ZERO) <= 0) {
//                throw new IllegalArgumentException("Price must be positive");
//            }
//            if (price.scale() > 2 || price.precision() > 10) {
//                throw new IllegalArgumentException("Price must have at most 8 integer digits and 2 decimal places");
//            }
//        }
//        if (category != null) {
//            if (category.trim().isEmpty()) {
//                throw new IllegalArgumentException("Category must not be empty");
//            }
//            if (category.length() > 100) {
//                throw new IllegalArgumentException("Category must not exceed 100 characters");
//            }
//        }
//        if (stockQuantity != null && stockQuantity < 0) {
//            throw new IllegalArgumentException("Stock quantity must not be negative");
//        }
//        if (imageUrl != null && imageUrl.length() > 255) {
//            throw new IllegalArgumentException("Image URL must not exceed 255 characters");
//        }
//    }
}