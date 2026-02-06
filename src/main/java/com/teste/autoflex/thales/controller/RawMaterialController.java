package com.teste.autoflex.thales.controller;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import com.teste.autoflex.thales.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Data
@RequestMapping("/material")
public class RawMaterialController {

    @Autowired
    private RawMaterialService service;

    @PostMapping("/create")
    public ResponseEntity<RawMaterial> create(@RequestBody @Valid RawMaterialDTO dto){
        var material = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(material);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
            service.delete(id);
            return ResponseEntity.ok("Material deleted Successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<RawMaterial> update(@RequestParam String name, Double quantity){
      RawMaterial rawMaterial =  service.update(name, quantity);
        return ResponseEntity.ok(rawMaterial);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RawMaterialDTO>> search(@RequestParam String name){
        List<RawMaterial> list = service.search(name);
        List<RawMaterialDTO> listDTO = list
                .stream()
                .map(obj -> new RawMaterialDTO(obj.getId(), obj.getName(), obj.getStockQuantity()))
                .toList();

        return ResponseEntity.ok(listDTO);
    }
}
