package com.samara.emailsummary.ai.dto;

public record SummaryRequest(
        String assunto,
        String remetente,
        String conteudo
) {
}