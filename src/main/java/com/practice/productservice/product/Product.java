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
) {}