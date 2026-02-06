package com.teste.autoflex.thales.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        BigDecimal price,
        List<IngredientResponseDTO> ingredients
) {
}
