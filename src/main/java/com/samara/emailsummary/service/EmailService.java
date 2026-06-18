package com.samara.emailsummary.service;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.provider.EmailProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final EmailProvider emailProvider;

    public EmailService(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    public EmailResumoDTO obterEmailTeste() {
        return emailProvider.obterEmailTeste();
    }

    public List<EmailResumoDTO> listarEmails() {
        return emailProvider.listarEmails();
    }

    public EmailDetalheDTO buscarEmailPorId(String id) {
        return emailProvider.buscarEmailPorId(id);
    }
}