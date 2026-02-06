package com.teste.autoflex.thales.controller;

import com.teste.autoflex.thales.service.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;


}
