package com.samara.emailsummary.ai.service;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.AnalysisType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PromptBuilderServiceTest {

    @Test
    void deveConstruirPromptComDadosDoEmail() {

        PromptBuilderService service = new PromptBuilderService();

        SummaryRequest request = new SummaryRequest(
                AnalysisType.EMAIL_SUMMARY,
                "diretoria@sbot.org.br",
                "Reunião da Diretoria",
                "A reunião ocorrerá na próxima sexta-feira."
        );

        String prompt = service.construirPrompt(request);

        assertTrue(prompt.contains("diretoria@sbot.org.br"));
        assertTrue(prompt.contains("Reunião da Diretoria"));
        assertTrue(prompt.contains("A reunião ocorrerá na próxima sexta-feira."));
    }
}