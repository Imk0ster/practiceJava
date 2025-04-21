package com.practice.productservice.repository;

import com.practice.productservice.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query(value = "SELECT setval('products_id_seq', 1, false)", nativeQuery = true)
    void resetSequence();
}