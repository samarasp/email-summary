package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class DailyBriefingFormatterService {

    private static final DateTimeFormatter DATA_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter HORA_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public String formatar(DailyBriefing briefing) {

        StringBuilder texto = new StringBuilder();

        texto.append("═══════════════════════════════\n");
        texto.append("📨 BRIEFING DA MANHÃ\n\n");
        texto.append(briefing.data().format(DATA_FORMATTER))
                .append(" • ")
                .append(briefing.horario().format(HORA_FORMATTER))
                .append("\n");
        texto.append("═══════════════════════════════\n\n");

        texto.append("📌 O QUE MERECE SUA ATENÇÃO\n\n");
        texto.append("Este briefing apresenta os principais e-mails recebidos, organizados por prioridade.\n\n");

        texto.append("═══════════════════════════════\n\n");

        texto.append("📊 RESUMO GERAL\n\n");
        texto.append("• E-mails recebidos: ").append(briefing.totalEmails()).append("\n");
        texto.append("• Alta prioridade: ").append(briefing.altaPrioridade()).append("\n");
        texto.append("• Média prioridade: ").append(briefing.mediaPrioridade()).append("\n");
        texto.append("• Baixa prioridade: ").append(briefing.baixaPrioridade()).append("\n");
        texto.append("• Necessitam resposta: ").append(briefing.necessitamResposta()).append("\n");
        texto.append("• Com anexos: ").append(briefing.comAnexos()).append("\n\n");

        texto.append("═══════════════════════════════\n\n");

        adicionarSecaoPrioridade(texto, "🔴 PRIORIDADE ALTA", briefing.itens(), "Alta");
        adicionarSecaoPrioridade(texto, "🟡 PRIORIDADE MÉDIA", briefing.itens(), "Média");
        adicionarSecaoPrioridade(texto, "🟢 PRIORIDADE BAIXA", briefing.itens(), "Baixa");

        texto.append("Fim do briefing.\n");

        return texto.toString();
    }

    private void adicionarSecaoPrioridade(
            StringBuilder texto,
            String titulo,
            List<DailyBriefingItem> itens,
            String prioridade
    ) {
        List<DailyBriefingItem> filtrados = itens.stream()
                .filter(item -> normalizarPrioridade(item.resumo().prioridade())
                        .equals(normalizarPrioridade(prioridade)))
                .sorted(Comparator.comparing(DailyBriefingItem::numero))
                .toList();

        if (filtrados.isEmpty()) {
            return;
        }

        texto.append(titulo)
                .append(" (")
                .append(filtrados.size())
                .append(")\n\n");

        for (DailyBriefingItem item : filtrados) {
            adicionarItem(texto, item);
        }

        texto.append("═══════════════════════════════\n\n");
    }

    private void adicionarItem(StringBuilder texto, DailyBriefingItem item) {

        SummaryResponse resumo = item.resumo();

        texto.append("[").append(formatarNumero(item.numero())).append("]\n\n");

        texto.append("Remetente:\n");
        texto.append(item.email().getRemetente()).append("\n\n");

        texto.append("Assunto:\n");
        texto.append(item.email().getAssunto()).append("\n\n");

        texto.append("Resumo:\n");
        texto.append(valorOuPadrao(resumo.resumo())).append("\n\n");

        adicionarLista(texto, "Pendências", resumo.pendencias());
        adicionarLista(texto, "Ações sugeridas", resumo.acoesSugeridas());
        adicionarLista(texto, "Prazos", resumo.prazos());
        adicionarLista(texto, "Pessoas citadas", resumo.pessoasCitadas());

        texto.append("Necessita resposta:\n");
        texto.append(resumo.necessitaResposta() ? "SIM" : "Não").append("\n\n");

        if (resumo.necessitaResposta()) {
            texto.append("Sugestão de resposta:\n");
            texto.append(valorOuPadrao(resumo.sugestaoResposta())).append("\n\n");
        }

        texto.append("Nível de confiança:\n");
        texto.append(valorOuPadrao(resumo.nivelConfianca())).append("\n\n");

        texto.append("───────────────────────────────\n\n");
    }

    private void adicionarLista(StringBuilder texto, String titulo, List<String> itens) {

        if (itens == null || itens.isEmpty()) {
            return;
        }

        texto.append(titulo).append(":\n");

        for (String item : itens) {
            texto.append("• ").append(item).append("\n");
        }

        texto.append("\n");
    }

    private String formatarNumero(int numero) {
        return String.format("%02d", numero);
    }

    private String valorOuPadrao(String valor) {
        if (valor == null || valor.isBlank()) {
            return "Não informado.";
        }

        return valor;
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