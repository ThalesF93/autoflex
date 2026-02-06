package com.teste.autoflex.thales.controller;

import com.teste.autoflex.thales.dto.IngredientDTO;
import com.teste.autoflex.thales.dto.ProductDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.repository.ProductRepository;
import com.teste.autoflex.thales.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ProductService service;

    @MockitoBean
    ProductRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Must create product and inform its raw materials")
    void shouldCreateProduct() throws Exception{
        final String POST_ENDPOINT = "/product/create";

        UUID materialId = UUID.randomUUID();
        IngredientDTO ingredientDTO = new IngredientDTO(materialId, 2.0);
        ProductDTO dto = new ProductDTO(null, "Cerveja", new BigDecimal("15.00"), List.of(ingredientDTO));

        Product entity = new Product();
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        entity.setCompositions(new ArrayList<>());

        ProductResponseDTO responseDTO = new ProductResponseDTO(entity.getId(), entity.getName(), entity.getPrice(), List.of());

        Mockito.when(service.save(dto)).thenReturn(responseDTO);

        mvc.perform(MockMvcRequestBuilders
                        .post(POST_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(dto.name()));

        Mockito.verify(service, Mockito.times(1))
                .save(dto);
    }

    @Test
    @DisplayName("Testing annotation @Valid")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        final String POST_ENDPOINT = "/product/create";

        UUID materialId = UUID.randomUUID();
        IngredientDTO ingredientDTO = new IngredientDTO(materialId, 2.0);
        ProductDTO dto = new ProductDTO(null, "", new BigDecimal("15.00"), List.of(ingredientDTO));

        mvc.perform(MockMvcRequestBuilders
                        .post(POST_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).save(dto);

    }

}