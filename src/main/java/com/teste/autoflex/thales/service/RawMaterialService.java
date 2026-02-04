package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class RawMaterialService {

    @Autowired
    private RawMaterialRepository repository;

    @Transactional
    public RawMaterial save(RawMaterialDTO dto){
        RawMaterial entity = new RawMaterial();
        entity.setName(dto.name());
        entity.setStockQuantity(dto.stockQuantity());
        entity.setId(dto.id());
        repository.save(entity);
        return entity;
    }
}
