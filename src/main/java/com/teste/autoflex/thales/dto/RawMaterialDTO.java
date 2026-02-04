package com.teste.autoflex.thales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record RawMaterialDTO(

        UUID id,

        @NotBlank
        String name,

        @Positive
        Double stockQuantity


) {
}
