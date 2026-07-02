package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.ai.provider.GeminiProvider;
import com.samara.emailsummary.ai.service.BriefingPromptBuilderService;
import com.samara.emailsummary.briefing.context.DailyBriefingContextBuilder;
import com.samara.emailsummary.briefing.dto.BriefingAnalysis;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BriefingAnalysisService {

    private final DailyBriefingContextBuilder contextBuilder;
    private final BriefingPromptBuilderService promptBuilderService;
    private final GeminiProvider geminiProvider;

    public BriefingAnalysisService(
            DailyBriefingContextBuilder contextBuilder,
            BriefingPromptBuilderService promptBuilderService,
            GeminiProvider geminiProvider
    ) {
        this.contextBuilder = contextBuilder;
        this.promptBuilderService = promptBuilderService;
        this.geminiProvider = geminiProvider;
    }

    public BriefingAnalysis gerarAnalise(List<EmailDetalheDTO> emails) {

        String contexto = contextBuilder.construirContexto(emails);

        String prompt = promptBuilderService.construirPrompt(contexto);

        return geminiProvider.gerarAnalise(prompt);
    }
}