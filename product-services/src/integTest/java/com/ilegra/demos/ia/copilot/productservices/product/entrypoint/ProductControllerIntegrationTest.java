package com.ilegra.demos.ia.copilot.productservices.product.entrypoint;

import com.ilegra.demos.ia.copilot.productservices.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.UUID;

public class ProductControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // Chama o setup da base para garantir baseUrl
        super.setUpBase();
    }

    @Test
    void shouldCreateAndGetProduct() {
        ProductController.ProductDto dto = new ProductController.ProductDto("TestProduct");
        ResponseEntity<Product> createResponse = restTemplate.postForEntity(baseUrl, dto, Product.class);
        Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Product created = createResponse.getBody();
        Assertions.assertThat(created).isNotNull();
        Assertions.assertThat(created.getName()).isEqualTo("TestProduct");

        ResponseEntity<Product> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), Product.class);
        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product fetched = getResponse.getBody();
        Assertions.assertThat(fetched).isNotNull();
        Assertions.assertThat(fetched.getId()).isEqualTo(created.getId());
        Assertions.assertThat(fetched.getName()).isEqualTo("TestProduct");
    }

    @Test
    void shouldGetAllProducts() {
        ProductController.ProductDto dto1 = new ProductController.ProductDto("ProductA");
        ProductController.ProductDto dto2 = new ProductController.ProductDto("ProductB");
        restTemplate.postForEntity(baseUrl, dto1, Product.class);
        restTemplate.postForEntity(baseUrl, dto2, Product.class);

        ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl, Product[].class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldUpdateProduct() {
        ProductController.ProductDto dto = new ProductController.ProductDto("ProductToUpdate");
        ResponseEntity<Product> createResponse = restTemplate.postForEntity(baseUrl, dto, Product.class);
        Product created = createResponse.getBody();
        UUID id = created.getId();

        ProductController.ProductDto updateDto = new ProductController.ProductDto("UpdatedName");
        HttpEntity<ProductController.ProductDto> request = new HttpEntity<>(updateDto);
        ResponseEntity<Product> updateResponse = restTemplate.exchange(baseUrl + "/" + id, HttpMethod.PUT, request, Product.class);
        Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product updated = updateResponse.getBody();
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getName()).isEqualTo("UpdatedName");
    }

    @Test
    void shouldDeleteProduct() {
        ProductController.ProductDto dto = new ProductController.ProductDto("ProductToDelete");
        ResponseEntity<Product> createResponse = restTemplate.postForEntity(baseUrl, dto, Product.class);
        Product created = createResponse.getBody();
        UUID id = created.getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, null, Void.class);
        Assertions.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Product> getResponse = restTemplate.getForEntity(baseUrl + "/" + id, Product.class);
        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
