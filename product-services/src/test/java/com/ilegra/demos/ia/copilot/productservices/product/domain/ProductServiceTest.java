package com.ilegra.demos.ia.copilot.productservices.product.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProductServiceTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldCreateProduct() {
        String name = "UniqueProduct";
        Mockito.when(productRepository.existsByName(name)).thenReturn(false);
        Mockito.when(productRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        Product product = productService.createProduct(name);
        Assertions.assertThat(product.getName()).isEqualTo(name);
        Assertions.assertThat(product.getId()).isNotNull();
    }

    @Test
    void shouldNotCreateProductWithDuplicateName() {
        String name = "DuplicateProduct";
        Mockito.when(productRepository.existsByName(name)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> productService.createProduct(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unique");
    }

    @Test
    void shouldGetProductById() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Test");
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductById(id);
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFoundById() {
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Product> result = productService.getProductById(id);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = List.of(new Product(UUID.randomUUID(), "A"), new Product(UUID.randomUUID(), "B"));
        Mockito.when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getAllProducts();
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();
        Product existing = new Product(id, "OldName");
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        Mockito.when(productRepository.existsByName("NewName")).thenReturn(false);
        Mockito.when(productRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Product> updated = productService.updateProduct(id, "NewName");
        Assertions.assertThat(updated).isPresent();
        Assertions.assertThat(updated.get().getName()).isEqualTo("NewName");
    }

    @Test
    void shouldNotUpdateProductIfNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Product> updated = productService.updateProduct(id, "AnyName");
        Assertions.assertThat(updated).isEmpty();
    }

    @Test
    void shouldNotUpdateProductWithDuplicateName() {
        UUID id = UUID.randomUUID();
        Product existing = new Product(id, "OldName");
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        Mockito.when(productRepository.existsByName("DuplicateName")).thenReturn(true);
        Assertions.assertThatThrownBy(() -> productService.updateProduct(id, "DuplicateName"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unique");
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();
        Product existing = new Product(id, "ToDelete");
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        Mockito.doNothing().when(productRepository).deleteById(id);
        boolean deleted = productService.deleteProduct(id);
        Assertions.assertThat(deleted).isTrue();
    }

    @Test
    void shouldNotDeleteProductIfNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        boolean deleted = productService.deleteProduct(id);
        Assertions.assertThat(deleted).isFalse();
    }
}

