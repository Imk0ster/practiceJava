package com.practice.productservice.service;

import com.practice.productservice.product.Product;
import com.practice.productservice.product.ProductEntity;
import com.practice.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private void validateProduct(Product product, boolean isCreate) {
        if (isCreate) {
            if (product.name() == null || product.name().trim().isEmpty()) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            if (product.price() == null || product.price().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (product.category() == null || product.category().trim().isEmpty()) {
                throw new IllegalArgumentException("Category must not be empty");
            }
            if (product.isActive() == null) {
                throw new IllegalArgumentException("Active status is required");
            }
        } else {
            if (product.name() != null && product.name().trim().isEmpty()) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            if (product.price() != null && product.price().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (product.category() != null && product.category().trim().isEmpty()) {
                throw new IllegalArgumentException("Category must not be empty");
            }
        }

        if (product.name() != null && product.name().length() > 255) {
            throw new IllegalArgumentException("Name must not exceed 255 characters");
        }
        if (product.description() != null && product.description().length() > 255) {
            throw new IllegalArgumentException("Description must not exceed 255 characters");
        }
        if (product.price() != null && (product.price().scale() > 2 || product.price().precision() > 10)) {
            throw new IllegalArgumentException("Price must have at most 8 integer digits and 2 decimal places");
        }
        if (product.category() != null && product.category().length() > 100) {
            throw new IllegalArgumentException("Category must not exceed 100 characters");
        }
        if (product.stockQuantity() != null && product.stockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity must not be negative");
        }
        if (product.imageUrl() != null && product.imageUrl().length() > 255) {
            throw new IllegalArgumentException("Image URL must not exceed 255 characters");
        }
    }

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        logger.info("ProductServiceImpl initialized");
    }

    @Override
    public Product create(Product product) {
        logger.debug("Creating product with input: {}", product);
        try {
            logger.debug("Mapping Product to ProductEntity");
            validateProduct(product, true);
            LocalDateTime now = LocalDateTime.now();
            ProductEntity entity = new ProductEntity(
                    0, // id будет сгенерирован базой
                    product.name(),
                    product.description(),
                    product.price(),
                    product.category(),
                    product.stockQuantity(),
                    product.imageUrl(),
                    now,
                    now,
                    product.isActive()
            );
            logger.debug("Saving ProductEntity to repository");
            productRepository.save(entity);
            logger.info("Product created successfully with id: {}", entity.getId());
            return toProduct(entity);
        } catch (Exception e) {
            logger.error("Failed to create product: {}. Error: {}", product, e.getMessage(), e);
            throw e;
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> readAll() {
        logger.debug("Reading all products");
        try {
            logger.debug("Fetching all ProductEntities from repository");
            List<Product> products = productRepository.findAll().stream()
                    .map(this::toProduct)
                    .collect(Collectors.toList());
            logger.info("Retrieved {} products", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Failed to read all products. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Product read(int id) {
        logger.debug("Reading product with id: {}", id);
        try {
            logger.debug("Fetching ProductEntity with id: {} from repository", id);
            Product product = productRepository.findById(id)
                    .map(this::toProduct)
                    .orElse(null);
            if (product != null) {
                logger.info("Product found with id: {}", id);
            } else {
                logger.warn("Product not found with id: {}", id);
            }
            return product;
        } catch (Exception e) {
            logger.error("Failed to read product with id: {}. Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Product update(Product product, int id) {
        logger.debug("Updating product with id: {} with input: {}", id, product);
        try {
            validateProduct(product, false);
            logger.debug("Fetching ProductEntity with id: {} for update", id);
            return productRepository.findById(id)
                    .map(existingEntity -> {
                        logger.debug("Mapping Product to ProductEntity for update");
                        ProductEntity updatedEntity = new ProductEntity(
                                id,
                                product.name() != null ? product.name() : existingEntity.getName(),
                                product.description() != null ? product.description() : existingEntity.getDescription(),
                                product.price() != null ? product.price() : existingEntity.getPrice(),
                                product.category() != null ? product.category() : existingEntity.getCategory(),
                                product.stockQuantity() != null ? product.stockQuantity() : existingEntity.getStockQuantity(),
                                product.imageUrl() != null ? product.imageUrl() : existingEntity.getImageUrl(),
                                existingEntity.getCreatedAt(),
                                LocalDateTime.now(),
                                product.isActive() != null ? product.isActive() : existingEntity.isActive()
                        );
                        logger.debug("Saving updated ProductEntity to repository");
                        ProductEntity savedEntity = productRepository.save(updatedEntity);
                        logger.info("Product updated successfully with id: {}", id);

                        return new Product(
                                savedEntity.getId(),
                                savedEntity.getName(),
                                savedEntity.getDescription(),
                                savedEntity.getPrice(),
                                savedEntity.getCategory(),
                                savedEntity.getStockQuantity(),
                                savedEntity.getImageUrl(),
                                savedEntity.getCreatedAt(),
                                savedEntity.getUpdatedAt(),
                                savedEntity.isActive()
                        );
                    })
                    .orElseGet(() -> {
                        logger.warn("Product not found for update with id: {}", id);
                        return null;
                    });
        } catch (Exception e) {
            logger.error("Failed to update product with id: {}. Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(int id) {
        logger.debug("Deleting product with id: {}", id);
        try {
            logger.debug("Checking if product exists with id: {}", id);
            if (productRepository.existsById(id)) {
                logger.debug("Deleting ProductEntity with id: {} from repository", id);
                productRepository.deleteById(id);
                logger.info("Product deleted successfully with id: {}", id);
                return true;
            }
            logger.warn("Product not found for deletion with id: {}", id);
            return false;
        } catch (Exception e) {
            logger.error("Failed to delete product with id: {}. Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private Product toProduct(ProductEntity entity) {
        logger.debug("Mapping ProductEntity to Product for id: {}", entity.getId());
        Product product = new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getStockQuantity(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive()
        );
        logger.debug("Mapped ProductEntity to Product: {}", product);
        return product;
    }
}