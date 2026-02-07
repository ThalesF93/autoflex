package com.teste.autoflex.thales.dto.update;

import com.teste.autoflex.thales.dto.IngredientDTO;

import java.math.BigDecimal;
import java.util.List;

public record ProductUpdateDTO(
        String name,

        BigDecimal price,

        List<IngredientUpdateDTO> ingredients
) {
}
