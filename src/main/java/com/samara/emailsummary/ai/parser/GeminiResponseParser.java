package com.samara.emailsummary.ai.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.briefing.dto.BriefingAnalysis;
import org.springframework.stereotype.Component;

@Component
public class GeminiResponseParser {

    private final ObjectMapper objectMapper;

    public GeminiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SummaryResponse parse(String respostaBruta) throws JsonProcessingException {
        String respostaLimpa = limparJson(respostaBruta);

        return objectMapper.readValue(
                respostaLimpa,
                SummaryResponse.class
        );
    }

    private String limparJson(String resposta) {
        if (resposta == null || resposta.isBlank()) {
            return "";
        }

        return resposta
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }

    public BriefingAnalysis parseBriefing(String respostaBruta) throws JsonProcessingException {

        String respostaLimpa = limparJson(respostaBruta);

        return objectMapper.readValue(
                respostaLimpa,
                BriefingAnalysis.class
        );
    }

}