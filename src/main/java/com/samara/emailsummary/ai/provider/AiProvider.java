package com.samara.emailsummary.ai.provider;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;

public interface AiProvider {

    SummaryResponse gerarResumo(SummaryRequest request);
}