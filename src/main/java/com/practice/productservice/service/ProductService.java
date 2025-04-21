package com.practice.productservice.service;

import com.practice.productservice.product.Product;

import java.util.List;

public interface ProductService {

    void create(Product product);

    List<Product> readAll();

    Product read(int id);

    boolean update(Product product, int id);

    boolean delete(int id);

    void delete();
}