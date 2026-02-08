package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.dto.response.RawMaterialResponseDTO;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Data
public class RawMaterialService {

    @Autowired
    private RawMaterialRepository repository;

    @Transactional
    public RawMaterialResponseDTO save(RawMaterialDTO dto){
        RawMaterial entity = new RawMaterial();
        entity.setName(dto.name());
        entity.setStockQuantity(dto.stockQuantity());
        entity.setId(dto.id());
        repository.save(entity);

        return new RawMaterialResponseDTO(entity.getName(), entity.getStockQuantity());
    }

    @Transactional
    public void delete(UUID id){
             var materialFound = repository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found"));
             repository.delete(materialFound);
    }

    @Transactional
    public RawMaterial update(String name, Double newQuantity){
       RawMaterial material = repository.findByName(name)
               .orElseThrow(() -> new MaterialNotFoundException("Material not found"));

       material.setStockQuantity(material.getStockQuantity() + newQuantity);

       return repository.save(material);
    }

    public List<RawMaterial> listAll(){
        return repository.findAllByOrderByNameAsc();
    }

    public List<RawMaterial>  search(String name){
        RawMaterial entity = new  RawMaterial();
        entity.setName(name);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<RawMaterial> materials = Example.of(entity, matcher);
        return repository.findAll(materials);
    }

    public static List<RawMaterialDTO> toListDTO(List<RawMaterial> products) {
        return products.stream().map(rm -> new RawMaterialDTO(rm.getId(), rm.getName(), rm.getStockQuantity())).toList();
    }
}
