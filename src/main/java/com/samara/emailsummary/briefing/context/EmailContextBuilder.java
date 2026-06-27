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

        List<EmailDetalheDTO> historico = selecionarHistoricoAnterior(emailAtual, emailsDaThread);

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

    private List<EmailDetalheDTO> selecionarHistoricoAnterior(
            EmailDetalheDTO emailAtual,
            List<EmailDetalheDTO> emailsDaThread
    ) {
        if (emailsDaThread == null || emailsDaThread.isEmpty()) {
            return List.of();
        }

        int indiceEmailAtual = -1;

        for (int i = 0; i < emailsDaThread.size(); i++) {
            EmailDetalheDTO email = emailsDaThread.get(i);

            if (email.getId() != null && email.getId().equals(emailAtual.getId())) {
                indiceEmailAtual = i;
                break;
            }
        }

        if (indiceEmailAtual <= 0) {
            return List.of();
        }

        int inicio = Math.max(0, indiceEmailAtual - MAX_EMAILS_CONTEXTO);

        return emailsDaThread.subList(inicio, indiceEmailAtual);
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