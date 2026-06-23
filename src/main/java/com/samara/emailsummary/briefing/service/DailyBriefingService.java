package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DailyBriefingService {

    public DailyBriefing criarBriefing(List<DailyBriefingItem> itens) {

        int totalEmails = itens.size();

        int altaPrioridade = contarPorPrioridade(itens, "Alta");
        int mediaPrioridade = contarPorPrioridade(itens, "Média");
        int baixaPrioridade = contarPorPrioridade(itens, "Baixa");

        int necessitamResposta = (int) itens.stream()
                .filter(item -> item.resumo().necessitaResposta())
                .count();

        int comAnexos = (int) itens.stream()
                .filter(item -> item.email().isTemAnexo())
                .count();

        return new DailyBriefing(
                LocalDate.now(),
                LocalTime.now(),
                totalEmails,
                altaPrioridade,
                mediaPrioridade,
                baixaPrioridade,
                necessitamResposta,
                comAnexos,
                itens
        );
    }

    private int contarPorPrioridade(List<DailyBriefingItem> itens, String prioridade) {
        return (int) itens.stream()
                .filter(item -> normalizarPrioridade(item.resumo().prioridade())
                        .equals(normalizarPrioridade(prioridade)))
                .count();
    }

    private String normalizarPrioridade(String prioridade) {
        if (prioridade == null) {
            return "";
        }

        return prioridade
                .trim()
                .toLowerCase()
                .replace("é", "e");
    }
}