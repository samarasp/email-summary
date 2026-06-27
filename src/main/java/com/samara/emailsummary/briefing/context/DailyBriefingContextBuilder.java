package com.samara.emailsummary.briefing.context;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyBriefingContextBuilder {

    private static final int LIMITE_EMAILS = 20;
    private static final int LIMITE_CARACTERES_POR_EMAIL = 3000;

    public String construirContexto(List<EmailDetalheDTO> emails) {

        StringBuilder contexto = new StringBuilder();

        int totalRecebidos = emails.size();
        List<EmailDetalheDTO> emailsSelecionados = limitarEmails(emails);

        contexto.append("BRIEFING DIÁRIO DE E-MAILS\n\n");
        contexto.append("Quantidade de e-mails recebidos: ")
                .append(totalRecebidos)
                .append("\n");

        contexto.append("Quantidade de e-mails enviados para análise: ")
                .append(emailsSelecionados.size())
                .append("\n");

        if (totalRecebidos > emailsSelecionados.size()) {
            contexto.append("Observação: alguns e-mails foram omitidos por limite de segurança do contexto.\n");
        }

        contexto.append("\n");

        int numero = 1;

        for (EmailDetalheDTO email : emailsSelecionados) {
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

            contexto.append("Tem anexo:\n")
                    .append(email.isTemAnexo() ? "Sim" : "Não")
                    .append("\n\n");

            contexto.append("Conteúdo:\n")
                    .append(limitarTexto(valorOuVazio(email.getCorpo()), LIMITE_CARACTERES_POR_EMAIL))
                    .append("\n\n");

            numero++;
        }

        return contexto.toString().trim();
    }

    private List<EmailDetalheDTO> limitarEmails(List<EmailDetalheDTO> emails) {
        if (emails.size() <= LIMITE_EMAILS) {
            return emails;
        }

        return emails.subList(0, LIMITE_EMAILS);
    }

    private String limitarTexto(String texto, int limite) {
        if (texto.length() <= limite) {
            return texto;
        }

        return texto.substring(0, limite)
                + "\n\n[Conteúdo truncado por limite de segurança do contexto.]";
    }

    private String valorOuVazio(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim();
    }
}