package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.briefing.dto.BriefingClassification;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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

        texto.append("📊 RESUMO GERAL\n\n");
        texto.append("Foram identificados ")
                .append(briefing.totalEmails())
                .append(" e-mails relevantes para análise nesta execução.\n\n");

        texto.append("📈 NÚMEROS DO DIA\n\n");
        texto.append("• E-mails relevantes: ").append(briefing.totalEmails()).append("\n");
        texto.append("• Exigem ação: ").append(briefing.exigeAcao()).append("\n");
        texto.append("• Apenas acompanhar: ").append(briefing.acompanhar()).append("\n");
        texto.append("• Somente informação: ").append(briefing.informativos()).append("\n\n");

        texto.append("═══════════════════════════════\n\n");

        texto.append("📌 E-MAILS RELEVANTES\n\n");

        adicionarSecao(
                texto,
                "🔴 EXIGE AÇÃO",
                briefing.itens(),
                BriefingClassification.EXIGE_ACAO
        );

        adicionarSecao(
                texto,
                "🟡 APENAS ACOMPANHAR",
                briefing.itens(),
                BriefingClassification.ACOMPANHAR
        );

        adicionarSecao(
                texto,
                "🔵 SOMENTE INFORMAÇÃO",
                briefing.itens(),
                BriefingClassification.INFORMATIVO
        );

        if (briefing.itens().isEmpty()) {
            texto.append("Nenhum e-mail relevante foi identificado nesta execução.\n\n");
        }

        texto.append("Fim do briefing.\n");

        return texto.toString();
    }

    private void adicionarSecao(
            StringBuilder texto,
            String titulo,
            List<DailyBriefingItem> itens,
            BriefingClassification classificacao
    ) {
        List<DailyBriefingItem> filtrados = itens.stream()
                .filter(item -> item.classificacao() == classificacao)
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
        texto.append("[").append(formatarNumero(item.numero())).append("]\n\n");

        texto.append("Remetente:\n");
        texto.append(valorOuPadrao(item.remetente())).append("\n\n");

        texto.append("Assunto:\n");
        texto.append(valorOuPadrao(item.assunto())).append("\n\n");

        texto.append("Resumo:\n");
        texto.append(valorOuPadrao(item.resumo())).append("\n\n");

        texto.append("Próximas ações:\n");
        texto.append(valorOuPadrao(item.proximasAcoes())).append("\n\n");

        texto.append("───────────────────────────────\n\n");
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
}