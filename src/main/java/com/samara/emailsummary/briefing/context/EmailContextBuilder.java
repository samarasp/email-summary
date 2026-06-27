package com.samara.emailsummary.briefing.context;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.service.ThreadContextService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailContextBuilder {

    private static final int MAX_EMAILS_CONTEXTO = 3;

    private final ThreadContextService threadContextService;

    public EmailContextBuilder(ThreadContextService threadContextService) {
        this.threadContextService = threadContextService;
    }

    public EmailContext criarContexto(EmailDetalheDTO email) {
        List<EmailDetalheDTO> emailsDaThread = threadContextService
                .recuperarContextoPorThread(email.getThreadId());

        String conteudoParaIA = montarConteudo(email, emailsDaThread);

        return new EmailContext(
                email,
                conteudoParaIA
        );
    }

    private String montarConteudo(EmailDetalheDTO emailAtual, List<EmailDetalheDTO> emailsDaThread) {
        StringBuilder contexto = new StringBuilder();

        contexto.append("""
                DADOS DO E-MAIL ATUAL

                Remetente:
                %s

                Assunto:
                %s

                Data:
                %s

                Corpo:
                %s
                """.formatted(
                valorOuVazio(emailAtual.getRemetente()),
                valorOuVazio(emailAtual.getAssunto()),
                valorOuVazio(emailAtual.getData()),
                valorOuVazio(emailAtual.getCorpo())
        ));

        List<EmailDetalheDTO> historico = emailsDaThread.stream()
                .filter(email -> email.getId() != null)
                .filter(email -> !email.getId().equals(emailAtual.getId()))
                .limit(MAX_EMAILS_CONTEXTO)
                .toList();

        if (!historico.isEmpty()) {
            contexto.append("""

                    ==================== CONTEXTO DA THREAD ====================

                    Mensagens anteriores relacionadas:
                    """);

            for (EmailDetalheDTO email : historico) {
                contexto.append("""

                        ---
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
                        limitarTexto(valorOuVazio(email.getCorpo()), 1500)
                ));
            }
        }

        return contexto.toString();
    }

    private String valorOuVazio(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.trim();
    }

    private String limitarTexto(String texto, int limite) {
        if (texto == null || texto.length() <= limite) {
            return texto;
        }

        return texto.substring(0, limite) + "\n\n[Texto truncado por limite de contexto]";
    }
}