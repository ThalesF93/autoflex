package com.teste.autoflex.thales.dto.entitiesDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record ProductDTO(

        UUID id,

        @NotBlank
        String name,

        @NotNull
        @Positive
        BigDecimal price,

        List<IngredientDTO> ingredients) {
}
