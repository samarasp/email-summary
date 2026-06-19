package com.samara.emailsummary.ai.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GeminiResponseParserTest {

    @Test
    void deveConverterJsonValidoEmSummaryResponse() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        GeminiResponseParser parser = new GeminiResponseParser(objectMapper);

        String json = """
                {
                  "resumo": "Resumo do e-mail",
                  "prioridade": "Baixa",
                  "acoesSugeridas": [],
                  "pendencias": [],
                  "prazos": [],
                  "pessoasCitadas": [],
                  "necessitaResposta": false,
                  "sugestaoResposta": "",
                  "nivelConfianca": "Alta"
                }
                """;

        SummaryResponse resposta = parser.parse(json);

        assertEquals("Resumo do e-mail", resposta.resumo());
        assertEquals("Baixa", resposta.prioridade());
        assertFalse(resposta.necessitaResposta());
        assertEquals("Alta", resposta.nivelConfianca());
    }

    @Test
    void deveConverterJsonComBlocoMarkdownEmSummaryResponse() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        GeminiResponseParser parser = new GeminiResponseParser(objectMapper);

        String json = """
            ```json
            {
              "resumo": "Resumo vindo do Gemini",
              "prioridade": "Média",
              "acoesSugeridas": [],
              "pendencias": [],
              "prazos": [],
              "pessoasCitadas": [],
              "necessitaResposta": true,
              "sugestaoResposta": "Obrigada pelo envio.",
              "nivelConfianca": "Alta"
            }
            ```
            """;

        SummaryResponse resposta = parser.parse(json);

        assertEquals("Resumo vindo do Gemini", resposta.resumo());
        assertEquals("Média", resposta.prioridade());
        assertEquals(true, resposta.necessitaResposta());
        assertEquals("Obrigada pelo envio.", resposta.sugestaoResposta());
    }

    @Test
    void deveLancarExcecaoQuandoJsonForInvalido() {
        ObjectMapper objectMapper = new ObjectMapper();
        GeminiResponseParser parser = new GeminiResponseParser(objectMapper);

        String jsonInvalido = "isso não é json";

        assertThrows(
                JsonProcessingException.class,
                () -> parser.parse(jsonInvalido)
        );
    }

    @Test
    void deveLancarExcecaoQuandoRespostaForNula() {
        ObjectMapper objectMapper = new ObjectMapper();
        GeminiResponseParser parser = new GeminiResponseParser(objectMapper);

        assertThrows(
                JsonProcessingException.class,
                () -> parser.parse(null)
        );
    }

}