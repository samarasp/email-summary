package com.samara.emailsummary.service;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.provider.EmailProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailboxContextService {

    private static final int MAX_EMAILS_CONTEXTO = 3;

    private final EmailProvider emailProvider;

    public MailboxContextService(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    public List<EmailDetalheDTO> recuperarEmailsRelacionados(EmailDetalheDTO emailAtual) {
        return emailProvider.buscarEmailsRelacionados(emailAtual)
                .stream()
                .filter(email -> email.getId() != null)
                .filter(email -> !email.getId().equals(emailAtual.getId()))
                .filter(email -> email.getThreadId() == null
                        || !email.getThreadId().equals(emailAtual.getThreadId()))
                .limit(MAX_EMAILS_CONTEXTO)
                .toList();
    }
}