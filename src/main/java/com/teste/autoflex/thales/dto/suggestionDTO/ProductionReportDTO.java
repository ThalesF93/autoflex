package com.teste.autoflex.thales.dto.suggestionDTO;

import java.math.BigDecimal;
import java.util.List;

public record ProductionReportDTO(
        List<ProductionSuggestionDTO> suggestions,
        BigDecimal totalEstimatedValue
) {
}
