package com.samara.emailsummary.ai.service;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.provider.AiProvider;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    private final AiProvider aiProvider;
    private final PromptBuilderService promptBuilderService;

    public SummaryService(
            AiProvider aiProvider,
            PromptBuilderService promptBuilderService
    ) {
        this.aiProvider = aiProvider;
        this.promptBuilderService = promptBuilderService;
    }

    public SummaryResponse gerarResumo(SummaryRequest request) {

        String prompt = promptBuilderService.construirPrompt(request);

        SummaryRequest requestComPrompt = new SummaryRequest(
                request.analysisType(),
                request.assunto(),
                request.remetente(),
                prompt
        );

        return aiProvider.gerarResumo(requestComPrompt);
    }
}