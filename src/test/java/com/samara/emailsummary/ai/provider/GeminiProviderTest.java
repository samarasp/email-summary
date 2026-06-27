package com.samara.emailsummary.ai.provider;

import com.samara.emailsummary.ai.client.GeminiClient;
import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.exception.AiCommunicationException;
import com.samara.emailsummary.ai.parser.GeminiResponseParser;
import com.samara.emailsummary.ai.dto.AnalysisType;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class GeminiProviderTest {

    @Test
    void deveGerarResumoComSucesso() throws Exception {
        GeminiClient geminiClient = mock(GeminiClient.class);
        GeminiResponseParser responseParser = mock(GeminiResponseParser.class);

        GeminiProvider provider = new GeminiProvider(geminiClient, responseParser);

        SummaryRequest request = new SummaryRequest(
                AnalysisType.EMAIL_SUMMARY,
                "diretoria@sbot.org.br",
                "Fatura IBDM",
                "Confirmo o recebimento da fatura."
        );

        SummaryResponse respostaEsperada = new SummaryResponse(
                "Confirmação de recebimento da fatura.",
                "Baixa",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                false,
                "",
                "Alta"
        );

        when(geminiClient.gerarConteudo(request.conteudo()))
                .thenReturn("json bruto do Gemini");

        when(responseParser.parse("json bruto do Gemini"))
                .thenReturn(respostaEsperada);

        SummaryResponse resposta = provider.gerarResumo(request);

        assertEquals("Confirmação de recebimento da fatura.", resposta.resumo());
        assertEquals("Baixa", resposta.prioridade());
        assertEquals("Alta", resposta.nivelConfianca());
    }

    @Test
    void deveRetornarRespostaErroQuandoGeminiClientLancarIOException() throws Exception {
        GeminiClient geminiClient = mock(GeminiClient.class);
        GeminiResponseParser responseParser = mock(GeminiResponseParser.class);

        GeminiProvider provider = new GeminiProvider(geminiClient, responseParser);

        SummaryRequest request = new SummaryRequest(
                AnalysisType.EMAIL_SUMMARY,
                "diretoria@sbot.org.br",
                "Fatura IBDM",
                "Confirmo o recebimento da fatura."
        );

        when(geminiClient.gerarConteudo(request.conteudo()))
                .thenThrow(new IOException("Falha simulada de comunicação"));

        SummaryResponse resposta = provider.gerarResumo(request);

        assertEquals("Erro ao comunicar com o serviço de IA.", resposta.resumo());
        assertEquals("Não classificada", resposta.prioridade());
        assertEquals("Baixa", resposta.nivelConfianca());
    }

    @Test
    void deveRetornarRespostaErroQuandoParserLancarJsonProcessingException() throws Exception {

        GeminiClient geminiClient = mock(GeminiClient.class);
        GeminiResponseParser responseParser = mock(GeminiResponseParser.class);

        GeminiProvider provider = new GeminiProvider(geminiClient, responseParser);

        SummaryRequest request = new SummaryRequest(
                AnalysisType.EMAIL_SUMMARY,
                "diretoria@sbot.org.br",
                "Fatura IBDM",
                "Confirmo o recebimento da fatura."
        );

        when(geminiClient.gerarConteudo(request.conteudo()))
                .thenReturn("json inválido");

        when(responseParser.parse("json inválido"))
                .thenThrow(new JsonProcessingException("JSON inválido") {});

        SummaryResponse resposta = provider.gerarResumo(request);

        assertEquals("Erro ao comunicar com o serviço de IA.", resposta.resumo());
        assertEquals("Não classificada", resposta.prioridade());
        assertEquals("Baixa", resposta.nivelConfianca());
    }

    @Test
    void deveLancarAiCommunicationExceptionQuandoGeminiClientLancarInterruptedException() throws Exception {

        GeminiClient geminiClient = mock(GeminiClient.class);
        GeminiResponseParser responseParser = mock(GeminiResponseParser.class);

        GeminiProvider provider = new GeminiProvider(geminiClient, responseParser);

        SummaryRequest request = new SummaryRequest(
                AnalysisType.EMAIL_SUMMARY,
                "diretoria@sbot.org.br",
                "Fatura IBDM",
                "Confirmo o recebimento da fatura."
        );

        when(geminiClient.gerarConteudo(request.conteudo()))
                .thenThrow(new InterruptedException("Chamada interrompida"));

        assertThrows(
                AiCommunicationException.class,
                () -> provider.gerarResumo(request)
        );

        assertTrue(Thread.currentThread().isInterrupted());

        Thread.interrupted();
    }

}