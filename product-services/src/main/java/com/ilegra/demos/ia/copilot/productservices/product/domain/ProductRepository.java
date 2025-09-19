package com.ilegra.demos.ia.copilot.productservices.product.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Optional<Product> findByName(String name);
    List<Product> findAll();
    void deleteById(UUID id);
    boolean existsByName(String name);
}

