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

    public EmailClassificationResult classificar(String assunto, String remetente, String corpo) {

        String texto = normalizar(
                (assunto == null ? "" : assunto)
                        + " "
                        + (remetente == null ? "" : remetente)
                        + " "
                        + (corpo == null ? "" : corpo)
        );

        for (String padrao : PADROES_NEWSLETTER) {

            if (texto.contains(padrao)) {

                return new EmailClassificationResult(
                        EmailCategory.NEWSLETTER,
                        "Identificado como newsletter por conter o padrão: \"" + padrao + "\"."
                );
            }
        }

        return new EmailClassificationResult(
                EmailCategory.ACTION_REQUIRED,
                "Nenhum padrão de newsletter foi identificado."
        );
    }

    private String normalizar(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);

        return textoNormalizado
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}