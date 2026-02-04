package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
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

import java.util.UUID;

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

        Mockito.when(repository.save(Mockito.any(RawMaterial.class))).thenReturn(entity);

        service.save(dto);

        Mockito.verify(repository).save(entity);
        Assertions.assertThat(dto.stockQuantity()).isEqualTo(entity.getStockQuantity());


    }

}