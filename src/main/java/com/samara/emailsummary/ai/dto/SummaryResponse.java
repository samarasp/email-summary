package com.samara.emailsummary.ai.dto;

import java.util.List;

public record SummaryResponse(
        String resumo,
        String prioridade,
        List<String> acoesSugeridas,
        List<String> pendencias,
        List<String> prazos,
        List<String> pessoasCitadas,
        boolean necessitaResposta,
        String sugestaoResposta,
        String nivelConfianca
) {
}