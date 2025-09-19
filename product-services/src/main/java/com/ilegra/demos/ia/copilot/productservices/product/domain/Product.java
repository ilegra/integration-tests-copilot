package com.ilegra.demos.ia.copilot.productservices.product.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "products", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
public class Product {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    protected Product() {}

    public Product(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

