package com.samara.emailsummary.ai.dto;

import com.samara.emailsummary.ai.dto.AnalysisType;

public record SummaryRequest(
        AnalysisType analysisType,
        String assunto,
        String remetente,
        String conteudo
) {
}