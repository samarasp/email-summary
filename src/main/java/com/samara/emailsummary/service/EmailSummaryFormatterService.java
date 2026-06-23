package com.samara.emailsummary.service;

import com.samara.emailsummary.ai.dto.SummaryResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailSummaryFormatterService {

    public String formatar(SummaryResponse resumo) {

        StringBuilder texto = new StringBuilder();

        texto.append("RESUMO DO E-MAIL\n");
        texto.append("=========================\n\n");

        texto.append("Resumo Executivo:\n");
        texto.append(resumo.resumo()).append("\n\n");

        texto.append("Prioridade: ")
                .append(resumo.prioridade())
                .append("\n\n");

        if (resumo.pendencias() != null && !resumo.pendencias().isEmpty()) {
            texto.append("Pendências:\n");
            resumo.pendencias().forEach(item ->
                    texto.append("• ").append(item).append("\n")
            );
            texto.append("\n");
        }

        if (resumo.acoesSugeridas() != null && !resumo.acoesSugeridas().isEmpty()) {
            texto.append("Ações sugeridas:\n");
            resumo.acoesSugeridas().forEach(item ->
                    texto.append("• ").append(item).append("\n")
            );
            texto.append("\n");
        }

        if (resumo.prazos() != null && !resumo.prazos().isEmpty()) {
            texto.append("Prazos:\n");
            resumo.prazos().forEach(item ->
                    texto.append("• ").append(item).append("\n")
            );
            texto.append("\n");
        }

        if (resumo.pessoasCitadas() != null && !resumo.pessoasCitadas().isEmpty()) {
            texto.append("Pessoas citadas:\n");
            resumo.pessoasCitadas().forEach(item ->
                    texto.append("• ").append(item).append("\n")
            );
            texto.append("\n");
        }

        texto.append("Necessita resposta: ")
                .append(resumo.necessitaResposta() ? "Sim" : "Não")
                .append("\n");

        if (resumo.necessitaResposta()
                && resumo.sugestaoResposta() != null
                && !resumo.sugestaoResposta().isBlank()) {

            texto.append("\nSugestão de resposta:\n");
            texto.append(resumo.sugestaoResposta()).append("\n");
        }

        texto.append("\nNível de confiança: ")
                .append(resumo.nivelConfianca());

        return texto.toString();
    }
}