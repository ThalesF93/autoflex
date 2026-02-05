package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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
        Assertions.assertThat(dto.stockQuantity()).isEqualTo(entity.getStockQuantity());

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
    }

}