package com.samara.emailsummary.briefing.dto;

public record BriefingEmailItem(

        String id,

        BriefingClassification classificacao,

        String remetente,

        String assunto,

        String resumo,

        String proximasAcoes

) {
}