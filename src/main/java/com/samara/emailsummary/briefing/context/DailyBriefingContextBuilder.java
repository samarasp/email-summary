package com.samara.emailsummary.briefing.context;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyBriefingContextBuilder {

    public String construirContexto(List<EmailDetalheDTO> emails) {

        StringBuilder contexto = new StringBuilder();

        contexto.append("BRIEFING DIÁRIO DE E-MAILS\n\n");
        contexto.append("Quantidade de e-mails analisados: ")
                .append(emails.size())
                .append("\n\n");

        int numero = 1;

        for (EmailDetalheDTO email : emails) {
            contexto.append("========================================\n");
            contexto.append("E-MAIL ").append(numero).append("\n");
            contexto.append("========================================\n\n");

            contexto.append("Remetente:\n")
                    .append(valorOuVazio(email.getRemetente()))
                    .append("\n\n");

            contexto.append("Assunto:\n")
                    .append(valorOuVazio(email.getAssunto()))
                    .append("\n\n");

            contexto.append("Data:\n")
                    .append(valorOuVazio(email.getData()))
                    .append("\n\n");

            contexto.append("Conteúdo:\n")
                    .append(valorOuVazio(email.getCorpo()))
                    .append("\n\n");

            numero++;
        }

        return contexto.toString().trim();
    }

    private String valorOuVazio(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim();
    }
}