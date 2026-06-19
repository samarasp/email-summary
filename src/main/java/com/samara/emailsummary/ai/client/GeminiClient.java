package com.samara.emailsummary.ai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samara.emailsummary.ai.dto.GeminiRequest;
import com.samara.emailsummary.ai.dto.GeminiResponse;
import com.samara.emailsummary.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class GeminiClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);

    private final HttpClient httpClient;
    private final AiProperties aiProperties;
    private final ObjectMapper objectMapper;

    public GeminiClient(HttpClient httpClient,
                        AiProperties aiProperties,
                        ObjectMapper objectMapper) {

        this.httpClient = httpClient;
        this.aiProperties = aiProperties;
        this.objectMapper = objectMapper;
    }

    public String gerarConteudo(String prompt) throws IOException, InterruptedException {

        log.info("Iniciando chamada ao Gemini.");

        if (!aiProperties.isEnabled()) {
            log.info("IA desativada.");
            return "IA desativada nas configurações.";
        }

        GeminiRequest geminiRequest = new GeminiRequest(prompt);

        String json = objectMapper.writeValueAsString(geminiRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiProperties.getGeminiApiUrl()))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", aiProperties.getGeminiApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        log.info("Requisição HTTP criada.");

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        log.info("Resposta recebida. Status HTTP: {}", response.statusCode());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            log.warn("Falha na chamada ao Gemini. Status HTTP: {}", response.statusCode());
            throw new IOException("Erro ao chamar Gemini.");
        }

        log.info("Resposta recebida com sucesso.");

        GeminiResponse geminiResponse = objectMapper.readValue(
                response.body(),
                GeminiResponse.class
        );

        return geminiResponse
                .getCandidates()
                .getFirst()
                .getContent()
                .getParts()
                .getFirst()
                .getText();
    }
}