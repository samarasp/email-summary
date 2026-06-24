package com.samara.emailsummary.briefing.context;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import org.springframework.stereotype.Component;

@Component
public class EmailContextBuilder {

    public EmailContext criarContexto(EmailDetalheDTO email) {
        String conteudoParaIA = montarConteudo(email);

        return new EmailContext(
                email,
                conteudoParaIA
        );
    }

    private String montarConteudo(EmailDetalheDTO email) {
        return """
                DADOS DO E-MAIL

                Remetente:
                %s

                Assunto:
                %s

                Data:
                %s

                Corpo:
                %s
                """.formatted(
                valorOuVazio(email.getRemetente()),
                valorOuVazio(email.getAssunto()),
                valorOuVazio(email.getData()),
                valorOuVazio(email.getCorpo())
        );
    }

    private String valorOuVazio(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim();
    }
}