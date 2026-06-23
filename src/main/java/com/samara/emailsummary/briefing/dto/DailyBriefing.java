package com.samara.emailsummary.briefing.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DailyBriefing(

        LocalDate data,

        LocalTime horario,

        int totalEmails,

        int altaPrioridade,

        int mediaPrioridade,

        int baixaPrioridade,

        int necessitamResposta,

        int comAnexos,

        List<DailyBriefingItem> itens

) {
}