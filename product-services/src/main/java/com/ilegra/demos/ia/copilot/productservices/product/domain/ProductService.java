package com.ilegra.demos.ia.copilot.productservices.product.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(String name) {
        if (productRepository.existsByName(name)) {
            logger.warn("Product with name '{}' already exists", name);
            throw new IllegalArgumentException("Product name must be unique");
        }
        Product product = new Product(UUID.randomUUID(), name);
        Product saved = productRepository.save(product);
        logger.info("Product created: {}", saved.getId());
        return saved;
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Optional<Product> updateProduct(UUID id, String name) {
        Optional<Product> existingOpt = productRepository.findById(id);
        if (existingOpt.isEmpty()) {
            logger.warn("Product with id '{}' not found for update", id);
            return Optional.empty();
        }
        if (productRepository.existsByName(name)) {
            logger.warn("Product with name '{}' already exists", name);
            throw new IllegalArgumentException("Product name must be unique");
        }
        Product existing = existingOpt.get();
        Product updated = new Product(existing.getId(), name);
        Product saved = productRepository.save(updated);
        logger.info("Product updated: {}", saved.getId());
        return Optional.of(saved);
    }

    @Transactional
    public boolean deleteProduct(UUID id) {
        if (productRepository.findById(id).isEmpty()) {
            logger.warn("Product with id '{}' not found for deletion", id);
            return false;
        }
        productRepository.deleteById(id);
        logger.info("Product deleted: {}", id);
        return true;
    }
}

