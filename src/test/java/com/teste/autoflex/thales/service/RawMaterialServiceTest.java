package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.model.RawMaterial;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @InjectMocks
    RawMaterialService service;

    @Mock
    RawMaterialRepository repository;

    @Test
    @DisplayName("Must create and save new raw product Successfully")
    void mustSaveRawProduct(){

        RawMaterialDTO dto = new RawMaterialDTO(UUID.randomUUID(), "Name", 41984D);
        RawMaterial entity = new RawMaterial();
        entity.setId(dto.id());
        entity.setName(dto.name());
        entity.setStockQuantity(dto.stockQuantity());

        when(repository.save(Mockito.any(RawMaterial.class))).thenReturn(entity);

        service.save(dto);

        Mockito.verify(repository).save(entity);
        assertThat(dto.stockQuantity()).isEqualTo(entity.getStockQuantity());

    }

    @Test
    @DisplayName("Must delete a product by ID")
    void mustDeleteById(){

        RawMaterial material = new RawMaterial();
        material.setId(UUID.randomUUID());

        when(repository.findById(material.getId())).thenReturn(Optional.of(material));
        doNothing().when(repository).delete(material);

        service.delete(material.getId());
        verify(repository).delete(material);
    }

    @Test
    @DisplayName("Must Throw Customized Exception when ID not found ")
    void mustThrowException(){
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(MaterialNotFoundException.class)
                .isThrownBy(()->service.delete(uuid)).withMessage("Material not found");

        verify(repository, never()).delete(any(RawMaterial.class));
    }

    @Test
    @DisplayName("Must update the product quantity")
    void mustUpdateQuantity(){

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName("test");
        rawMaterial.setStockQuantity(100D);

        when(repository.findByName(rawMaterial.getName())).thenReturn(rawMaterial);
        when(repository.save(Mockito.any(RawMaterial.class))).thenReturn(rawMaterial);

        service.update(rawMaterial.getName(), rawMaterial.getStockQuantity());

        verify(repository).save(any(RawMaterial.class));
        verify(repository).findByName(any(String.class));
    }

    @Test
    @DisplayName("Must Throw Customized Exception when name not found ")
    void mustThrowExceptionWhenNameNotFound(){
        String name = "test";

        Assertions.assertThatExceptionOfType(MaterialNotFoundException.class)
                .isThrownBy(()->service.update(name, anyDouble())).withMessage("Material not found");
    }

    @Test
    @DisplayName("Must get and show products")
    void mustGetMaterials(){
        String searchName = "example";
    
        RawMaterial material1 = new RawMaterial();
        material1.setId(UUID.randomUUID());
        material1.setName("Arroz");
        material1.setStockQuantity(100D);
    
        RawMaterial material2 = new RawMaterial();
        material2.setId(UUID.randomUUID());
        material2.setName("Feijao");
        material2.setStockQuantity(200D);
    
        List<RawMaterial> expectedMaterials = List.of(material1, material2);
    
        when(repository.findAll(any(Example.class))).thenReturn(expectedMaterials);

        List<RawMaterial> result = service.search(searchName);

        verify(repository).findAll(any(Example.class));
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(material1, material2);
    }

    @Test
    @DisplayName("Must return ordered list of all products")
    void mustReturnListOfAllProductsByAsc(){

        RawMaterial p1 = new RawMaterial();
        p1.setName("Leite");

        RawMaterial p2 = new RawMaterial();
        p2.setName("milho");

        when(repository.findAllByOrderByNameAsc()).thenReturn(List.of(p1, p2));

        var result = service.listAll();

        Assertions.assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting("name")
                .containsExactly("Leite", "milho");

        verify(repository, times(1)).findAllByOrderByNameAsc();

    }
}