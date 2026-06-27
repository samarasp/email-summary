package com.samara.emailsummary.provider;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;

import java.util.List;

public interface EmailProvider {

    EmailResumoDTO obterEmailTeste();

    List<EmailResumoDTO> listarEmails();

    EmailDetalheDTO buscarEmailPorId(String id);

    List<EmailDetalheDTO> buscarEmailsPorThreadId(String threadId);
}