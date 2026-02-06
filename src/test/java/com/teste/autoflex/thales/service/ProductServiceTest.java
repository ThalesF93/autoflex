package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.IngredientDTO;
import com.teste.autoflex.thales.dto.ProductDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
import com.teste.autoflex.thales.exceptions.DuplicatedRegisterException;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}