package com.samara.emailsummary.ai.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samara.emailsummary.ai.client.GeminiClient;
import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.parser.GeminiResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.samara.emailsummary.ai.exception.AiCommunicationException;

import java.io.IOException;
import java.util.List;

@Component
public class GeminiProvider implements AiProvider {

    private static final Logger log = LoggerFactory.getLogger(GeminiProvider.class);

    private final GeminiClient geminiClient;
    private final GeminiResponseParser responseParser;

    public GeminiProvider(
            GeminiClient geminiClient,
            GeminiResponseParser responseParser
    ) {
        this.geminiClient = geminiClient;
        this.responseParser = responseParser;
    }

    @Override
    public SummaryResponse gerarResumo(SummaryRequest request) {

        try {
            log.info("Iniciando geração de resumo com Gemini.");

            String respostaBruta = geminiClient.gerarConteudo(request.conteudo());

            SummaryResponse resumo = responseParser.parse(respostaBruta);

            log.info("Resumo gerado com sucesso pelo Gemini.");

            return resumo;

        } catch (JsonProcessingException e) {
            log.error("Falha ao interpretar a resposta do Gemini.", e);
            return respostaErro();

        } catch (IOException e) {
            log.error("Erro de comunicação com o Gemini.", e);
            return respostaErro();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Comunicação com o Gemini interrompida.");
            throw new AiCommunicationException("Comunicação com o serviço de IA interrompida.", e);
        }
    }

    private SummaryResponse respostaErro() {
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