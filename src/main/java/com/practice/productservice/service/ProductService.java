package com.practice.productservice.service;

import com.practice.productservice.product.Product;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> readAll();

    Product read(int id);

    Product update(Product product, int id);

    boolean delete(int id);

}