package com.practice.productservice.controller;

import com.practice.productservice.product.Product;
import com.practice.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "API for managing products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Adds a new product to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> create(@RequestBody @Parameter(description = "Product details") Product product) {
        logger.debug("Received POST /api/products with body: {}", product);
        try {
            logger.debug("Received POST /api/products with body: {}", product);
            Product createdProduct = productService.create(product);
            logger.info("Product created successfully, returning 201 Created with product: {}", createdProduct);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to process POST /api/products with body: {}. Error: {}", product, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of products", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "No products found")
    })
    public ResponseEntity<List<Product>> read() {
        logger.debug("Received GET /api/products");
        try {
            logger.debug("Calling ProductService.readAll");
            final List<Product> products = productService.readAll();
            if (products != null && !products.isEmpty()) {
                logger.info("Returning {} products, status 200 OK", products.size());
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                logger.warn("No products found, returning 404 Not Found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Failed to process GET /api/products. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> read(@PathVariable(name = "id") @Parameter(description = "Product ID") int id) {
        logger.debug("Received GET /api/products/{} with id: {}", id, id);
        try {
            logger.debug("Calling ProductService.read with id: {}", id);
            final Product product = productService.read(id);
            if (product != null) {
                logger.info("Product found with id: {}, returning 200 OK", id);
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                logger.warn("Product not found with id: {}, returning 404 Not Found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Failed to process GET /api/products/{} with id: {}. Error: {}", id, id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product by its ID with partial or full data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "304", description = "Product not found or not updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> update(@PathVariable(name = "id") @Parameter(description = "Product ID") int id,
                                    @RequestBody @Parameter(description = "Updated product details") Product product) {
        logger.debug("Received PUT /api/products/{} with id: {} and body: {}", id, id, product);
        try {
            logger.debug("Calling ProductService.update with id: {} and product: {}", id, product);
            Product updatedProduct = productService.update(product, id);
            if (updatedProduct != null) {
                logger.info("Product updated successfully with id: {}, returning 200 OK", id);
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } else {
                logger.warn("Product not found or not updated with id: {}, returning 304 Not Modified", id);
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            logger.error("Failed to process PUT /api/products/{} with id: {} and body: {}. Error: {}", id, id, product, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "304", description = "Product not found")
    })
    public ResponseEntity<?> delete(@PathVariable(name = "id") @Parameter(description = "Product ID") int id) {
        logger.debug("Received DELETE /api/products/{} with id: {}", id, id);
        try {
            logger.debug("Calling ProductService.delete with id: {}", id);
            final boolean deleted = productService.delete(id);
            if (deleted) {
                logger.info("Product deleted successfully with id: {}, returning 200 OK", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.warn("Product not found for deletion with id: {}, returning 304 Not Modified", id);
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            logger.error("Failed to process DELETE /api/products/{} with id: {}. Error: {}", id, id, e.getMessage(), e);
            throw e;
        }
    }
}