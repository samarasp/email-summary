package com.samara.emailsummary.briefing.context;

import com.samara.emailsummary.dto.EmailDetalheDTO;

public record EmailContext(

        EmailDetalheDTO email,

        String conteudoParaIA

) {
}