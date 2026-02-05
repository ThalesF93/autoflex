package com.teste.autoflex.thales.controller;

import com.jayway.jsonpath.JsonPath;
import com.teste.autoflex.thales.dto.RawMaterialDTO;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import com.teste.autoflex.thales.service.RawMaterialService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RawMaterialController.class)
class RawMaterialControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    RawMaterialRepository repository;

    @MockitoBean
    RawMaterialService service;


    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("Must Create a material into database")
    void shouldCreateRawProduct() throws Exception{

        final String POST_ENDPOINT = "/material/create";

        RawMaterialDTO dto = new RawMaterialDTO(UUID.randomUUID(), "Name", 41984D);
        RawMaterial entity = new RawMaterial();
        entity.setId(dto.id());
        entity.setName(dto.name());
        entity.setStockQuantity(dto.stockQuantity());

        Mockito.when(service.save(dto)).thenReturn(entity);

        mvc.perform(MockMvcRequestBuilders
                        .post(POST_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(entity.getId().toString()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.stockQuantity").value(dto.stockQuantity()));
        Mockito.verify(service)
                .save(Mockito.any(RawMaterialDTO.class));
    }

    @Test
    @DisplayName("Must delete material")
    void shouldDeleteProduct() throws Exception {

        final String DELETE_ENDPOINT = "/material/{id}";

        UUID uuid = UUID.randomUUID();

        Mockito.doNothing().when(service).delete(uuid);

        mvc.perform(MockMvcRequestBuilders
                        .delete(DELETE_ENDPOINT, uuid))
                .andExpect(status().isOk());

        Mockito.verify(service).delete(uuid);

    }

    @Test
    @DisplayName("Must update product quantity")
    void shouldUpdateProduct() throws Exception{

        final String PUT_ENDPOINT = "/material/update";

        RawMaterial entity = new RawMaterial(UUID.randomUUID(), "algodao", 41984D);

        Mockito.when(repository.findByName(entity.getName())).thenReturn(entity);
        Mockito.when(service.update(entity.getName(), entity.getStockQuantity())).thenReturn(entity);
        Mockito.when(repository.save(Mockito.any(RawMaterial.class))).thenReturn(entity);

        mvc.perform(MockMvcRequestBuilders
                        .put(PUT_ENDPOINT)
                        .param("name", entity.getName())
                        .param( "quantity", entity.getStockQuantity().toString())
                )
                .andExpect(status().isOk());
        Mockito.verify(service)
                .update(entity.getName(), entity.getStockQuantity());


    }

}
