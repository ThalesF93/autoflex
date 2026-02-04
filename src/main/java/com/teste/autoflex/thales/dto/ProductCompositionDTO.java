package com.teste.autoflex.thales.dto;

import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.model.RawMaterial;

import java.util.UUID;

public record ProductCompositionDTO(

        UUID id,

        Product product,

        RawMaterial rawMaterial,

        Double requiredQuantity
) {
}
