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

import java.util.UUID;

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
}
