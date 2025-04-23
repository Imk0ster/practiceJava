package com.practice.productservice.repository;

import com.practice.productservice.product.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    @Query(value = "SELECT setval('products_id_seq', 1, false)", nativeQuery = true)
    default void resetSequence() {
        logger.debug("Resetting sequence 'products_id_seq' to 1");
        logger.info("Sequence 'products_id_seq' reset successfully");
    }
}