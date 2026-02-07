package com.teste.autoflex.thales.controller;

import com.teste.autoflex.thales.dto.ProductDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.service.ProductService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.teste.autoflex.thales.service.ProductService.convertListToDTO;

@RestController
@Data
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> save(@RequestBody @Valid ProductDTO dto){
        var product = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ProductResponseDTO>> findAll(){
        var products = service.listAll();
        var response = convertListToDTO(products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> search(@RequestParam String name){
        var list = service.search(name);
        List<ProductResponseDTO> listDTO = convertListToDTO(list);
        return ResponseEntity.ok(listDTO);
    }


}
