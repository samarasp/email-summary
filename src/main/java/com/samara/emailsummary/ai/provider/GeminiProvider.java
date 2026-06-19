package com.samara.emailsummary.ai.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samara.emailsummary.ai.client.GeminiClient;
import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminiProvider implements AiProvider {

    private static final Logger log = LoggerFactory.getLogger(GeminiProvider.class);

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    public GeminiProvider(GeminiClient geminiClient, ObjectMapper objectMapper) {
        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public SummaryResponse gerarResumo(SummaryRequest request) {

        try {
            String respostaBruta = geminiClient.gerarConteudo(request.conteudo());

            String respostaLimpa = limparJson(respostaBruta);

            return objectMapper.readValue(
                    respostaLimpa,
                    SummaryResponse.class
            );

        } catch (Exception e) {

            log.error("Falha ao gerar resumo com Gemini.", e);

            return new SummaryResponse(
                    "Erro ao comunicar com o serviço de IA.",
                    "Não classificada",
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    false,
                    "",
                    "Baixa"
            );
        }
    }

    private String limparJson(String resposta) {
        if (resposta == null) {
            return "";
        }

        return resposta
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }

}