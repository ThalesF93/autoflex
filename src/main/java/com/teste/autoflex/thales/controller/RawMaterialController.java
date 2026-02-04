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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/material")
public class RawMaterialController {

    @Autowired
    private RawMaterialRepository repository;

    @Autowired
    private RawMaterialService service;

    @PostMapping("/create")
    public ResponseEntity<RawMaterial> create(@RequestBody @Valid RawMaterialDTO dto){
        var material = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(material);
    }
}
