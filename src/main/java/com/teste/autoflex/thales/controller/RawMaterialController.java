package com.teste.autoflex.thales.controller;

import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
import com.teste.autoflex.thales.dto.response.RawMaterialResponseDTO;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.teste.autoflex.thales.service.ProductService.convertListToDTO;
import static com.teste.autoflex.thales.service.RawMaterialService.toListDTO;

@RestController
@Data
@RequestMapping("/material")
public class RawMaterialController {

    @Autowired
    private RawMaterialService service;

    @PostMapping("/create")
    public ResponseEntity<RawMaterialResponseDTO> create(@RequestBody @Valid RawMaterialDTO dto){
        var material = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(material);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
            service.delete(id);
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<RawMaterial> update(@RequestParam String name, Double quantity){
      RawMaterial rawMaterial =  service.update(name, quantity);
        return ResponseEntity.ok(rawMaterial);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RawMaterialDTO>> search(@RequestParam String name){
        List<RawMaterial> list = service.search(name);
        List<RawMaterialDTO> listDTO = toListDTO(list);

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RawMaterialDTO>> findAll() {
        var products = service.listAll();
        var response = toListDTO(products);
        return ResponseEntity.ok(response);
    }

}
