package com.samara.emailsummary.briefing.dto;

import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.dto.EmailDetalheDTO;

public record DailyBriefingItem(

        int numero,

        EmailDetalheDTO email,

        SummaryResponse resumo

) {
}