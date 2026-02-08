package com.teste.autoflex.thales.dto.update;

import java.math.BigDecimal;
import java.util.List;

public record ProductUpdateDTO(
        String name,

        BigDecimal price,

        List<IngredientUpdateDTO> ingredients
) {
}
