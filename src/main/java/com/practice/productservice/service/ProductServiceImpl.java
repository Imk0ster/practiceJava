package com.practice.productservice.service;

import com.practice.productservice.product.Product;
import com.practice.productservice.product.ProductEntity;
import com.practice.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void create(Product product) {
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
        productRepository.save(entity);
    }

    @Override
    public List<Product> readAll() {
        return productRepository.findAll().stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product read(int id) {
        return productRepository.findById(id)
                .map(this::toProduct)
                .orElse(null);
    }

    @Override
    public boolean update(Product product, int id) {
        return productRepository.findById(id)
                .map(existingEntity -> {
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
                    productRepository.save(updatedEntity);
                    return true;
                })
                .orElse(false);
    }

//    public boolean update(Product product, int id) {
//        return productRepository.findById(id)
//                .map(entity -> {
//                    if (product.name() != null) {
//                        entity.setName(product.name());
//                    }
//                    if (product.description() != null) {
//                        entity.setDescription(product.description());
//                    }
//                    if (product.price() != null) {
//                        entity.setPrice(product.price());
//                    }
//                    if (product.category() != null) {
//                        entity.setCategory(product.category());
//                    }
//                    if (product.stockQuantity() != null) {
//                        entity.setStockQuantity(product.stockQuantity());
//                    }
//                    if (product.imageUrl() != null) {
//                        entity.setImageUrl(product.imageUrl());
//                    }
//                    if (product.createdAt() != null) {
//                        entity.setCreatedAt(product.createdAt());
//                    }
//                    if (product.updatedAt() != null) {
//                        entity.setUpdatedAt(product.updatedAt());
//                    } else {
//                        entity.setUpdatedAt(LocalDateTime.now());
//                    }
//                    if (product.isActive() != null) {
//                        entity.setActive(product.isActive());
//                    }
//                    productRepository.save(entity);
//                    return true;
//                })
//                .orElse(false);
//    }
//        if (productRepository.existsById(id)) {
//            ProductEntity entity = new ProductEntity(
//                    id,
//                    product.name(),
//                    product.description(),
//                    product.price(),
//                    product.category(),
//                    product.stockQuantity(),
//                    product.imageUrl(),
//                    product.createdAt(),
//                    LocalDateTime.now(),
//                    product.isActive()
//            );
//            productRepository.save(entity);
//            return true;
//        }
//        return false;

    @Override
    public boolean delete(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void delete() {
        productRepository.deleteAll();
        productRepository.flush();
        productRepository.resetSequence();
    }

    private Product toProduct(ProductEntity entity) {
        return new Product(
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
    }
}