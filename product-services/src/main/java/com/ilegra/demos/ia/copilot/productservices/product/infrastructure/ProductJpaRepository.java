package com.ilegra.demos.ia.copilot.productservices.product.infrastructure;

import com.ilegra.demos.ia.copilot.productservices.product.domain.Product;
import com.ilegra.demos.ia.copilot.productservices.product.domain.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, UUID>, ProductRepository {
    Optional<Product> findByName(String name);
    boolean existsByName(String name);
    // Os demais métodos são herdados de JpaRepository e ProductRepository
}

