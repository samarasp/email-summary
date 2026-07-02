package com.samara.emailsummary.briefing.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DailyBriefing(

        LocalDate data,

        LocalTime horario,

        int totalEmails,

        int exigeAcao,

        int acompanhar,

        int informativos,

        List<DailyBriefingItem> itens

) {
}