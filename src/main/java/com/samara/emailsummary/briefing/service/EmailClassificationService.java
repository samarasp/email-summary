package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.briefing.dto.EmailCategory;
import com.samara.emailsummary.briefing.dto.EmailClassificationResult;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class EmailClassificationService {

    private static final List<String> PADROES_NEWSLETTER = List.of(
            "visualizar este e-mail como pagina web",
            "cancelar inscricao",
            "unsubscribe",
            "recebeu este e-mail porque",
            "nao deseja mais receber",
            "gerenciar preferencias"
    );

    private static final List<String> PADROES_CRITICOS = List.of(
            "urgente",
            "critico",
            "critica",
            "imediato",
            "imediata",
            "hoje",
            "prazo final",
            "vence hoje",
            "vencimento hoje"
    );

    private static final List<String> PADROES_ALTA_PRIORIDADE = List.of(
            "reuniao",
            "diretoria",
            "presidencia",
            "presidente",
            "prazo",
            "aprovacao",
            "deliberacao",
            "convocacao",
            "agenda",
            "passagens",
            "evento",
            "oficio",
            "documento"
    );

    private static final List<String> PADROES_INFORMATIVOS = List.of(
            "informe",
            "comunicado",
            "boletim",
            "newsletter",
            "noticias",
            "release"
    );

    public EmailClassificationResult classificar(String assunto, String remetente, String corpo) {

        String texto = normalizar(
                (assunto == null ? "" : assunto)
                        + " "
                        + (remetente == null ? "" : remetente)
                        + " "
                        + (corpo == null ? "" : corpo)
        );

        EmailClassificationResult newsletter = classificarPorPadrao(
                texto,
                PADROES_NEWSLETTER,
                EmailCategory.NEWSLETTER,
                "Identificado como newsletter"
        );

        if (newsletter != null) {
            return newsletter;
        }

        EmailClassificationResult critico = classificarPorPadrao(
                texto,
                PADROES_CRITICOS,
                EmailCategory.CRITICAL,
                "Identificado como crítico"
        );

        if (critico != null) {
            return critico;
        }

        EmailClassificationResult altaPrioridade = classificarPorPadrao(
                texto,
                PADROES_ALTA_PRIORIDADE,
                EmailCategory.HIGH_PRIORITY,
                "Identificado como alta prioridade"
        );

        if (altaPrioridade != null) {
            return altaPrioridade;
        }

        EmailClassificationResult informativo = classificarPorPadrao(
                texto,
                PADROES_INFORMATIVOS,
                EmailCategory.INFORMATIONAL,
                "Identificado como informativo"
        );

        if (informativo != null) {
            return informativo;
        }

        return new EmailClassificationResult(
                EmailCategory.ACTION_REQUIRED,
                "Nenhum padrão específico foi identificado; classificado como requer atenção."
        );
    }

    private EmailClassificationResult classificarPorPadrao(
            String texto,
            List<String> padroes,
            EmailCategory categoria,
            String justificativaBase
    ) {
        for (String padrao : padroes) {
            if (texto.contains(padrao)) {
                return new EmailClassificationResult(
                        categoria,
                        justificativaBase + " por conter o padrão: \"" + padrao + "\"."
                );
            }
        }

        return null;
    }

    private String normalizar(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);

        return textoNormalizado
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}