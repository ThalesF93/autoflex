package com.teste.autoflex.thales.dto.suggestionDTO;

import java.math.BigDecimal;

public record ProductionSuggestionDTO(
        String productName,
        Double quantity,
        BigDecimal unitPrice
) {
}
