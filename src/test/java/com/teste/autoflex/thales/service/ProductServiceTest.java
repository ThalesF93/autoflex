package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.IngredientDTO;
import com.teste.autoflex.thales.dto.ProductDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
import com.teste.autoflex.thales.exceptions.DuplicatedRegisterException;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
import com.teste.autoflex.thales.exceptions.ProductNotFoundException;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.ProductRepository;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService service;

    @Mock
    ProductRepository productRepository;

    @Mock
    RawMaterialRepository rawMaterialRepository;

    @Test
    @DisplayName("Must create a product with existing raw material")
    void mustCreateProduct(){
        UUID materialId = UUID.randomUUID();
        RawMaterial material = new RawMaterial();

        IngredientDTO ingredientDTO = new IngredientDTO(materialId, 2.0);
        ProductDTO dto = new ProductDTO(null, "Cerveja", new BigDecimal("15.00"), List.of(ingredientDTO));

        when(productRepository.existsProductByNameIgnoreCase(anyString())).thenReturn(false);
        when(rawMaterialRepository.findById(materialId)).thenReturn(Optional.of(material));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductResponseDTO result = service.save(dto);

        assertNotNull(result);
        assertEquals("Cerveja", result.name());
        verify(productRepository, Mockito.times(1)).save(any());
    }

    @Test
    @DisplayName("Must throw  Duplicated Product Exception")
    void mustThrowDuplicatedRegisterExceptionWhenCreateProduct(){
        ProductDTO dto = new ProductDTO(UUID.randomUUID(), "Cerveja", new BigDecimal("15.00"), List.of());

        when(productRepository.existsProductByNameIgnoreCase(anyString())).thenReturn(true);

        Assertions.assertThatExceptionOfType(DuplicatedRegisterException.class)
                .isThrownBy(()-> service.save(dto)).withMessage("Product with name: " + dto.name() + " already exists");
    }

    @Test
    @DisplayName("Must throw material not found exception")
    void mustThrowMaterialNotFoundExceptionWhenCreateProduct(){
        UUID materialId = UUID.randomUUID();
        IngredientDTO ingredientDTO = new IngredientDTO(materialId, 2.0);
        ProductDTO dto = new ProductDTO(UUID.randomUUID(), "Cerveja", new BigDecimal("15.00"), List.of(ingredientDTO));

        when(productRepository.existsProductByNameIgnoreCase(anyString())).thenReturn(false);
        when(rawMaterialRepository.findById(materialId)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(MaterialNotFoundException.class)
                .isThrownBy(()-> service.save(dto)).withMessage("Material not Found");
    }

    @Test
    @DisplayName("Must delete product successfully")
    void mustDeleteProduct(){
        UUID uuid = UUID.randomUUID();
        Product product = new Product();

        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        service.delete(uuid);
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when deleting non-existent product")
    void shouldThrowExceptionWhenProductNotFound() {
        UUID uuid = UUID.randomUUID();

        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(()-> service.delete(uuid)).withMessage("Product Not found");

        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    @DisplayName("Must return ordered list of all products")
    void mustReturnListOfAllProductsByAsc(){

        Product p1 = new Product();
        p1.setName("Chocolate");

        Product p2 = new Product();
        p2.setName("Abacaxi");

        when(productRepository.findAllByOrderByNameAsc()).thenReturn(List.of(p1, p2));

        var result = service.listAll();

        Assertions.assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting("name")
                .containsExactly("Chocolate", "Abacaxi");

        verify(productRepository, times(1)).findAllByOrderByNameAsc();

    }

    @Test
    @DisplayName("Must return search by example")
    void mustReturnSearchByExample(){
        String searchName = "example";

        Product product = new  Product();
        product.setId(UUID.randomUUID());
        product.setName("Test");

        Product product1 = new  Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Test1");

        List< Product> expectedMaterials = List.of(product, product1);

        when(productRepository.findAll(any(Example.class))).thenReturn(expectedMaterials);

        List<Product> result = service.search(searchName);

        verify(productRepository).findAll(any(Example.class));
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(product, product1);
    }
}