package com.teste.autoflex.thales.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record IngredientDTO(
        UUID rawMaterialId,

        @NotNull
        @Positive
        Double quantity
) {
}
