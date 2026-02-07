package com.teste.autoflex.thales.dto.update;

import java.util.UUID;

public record IngredientUpdateDTO(
        UUID rawMaterialId,
        String materialName,
        Double quantity
) {
}
