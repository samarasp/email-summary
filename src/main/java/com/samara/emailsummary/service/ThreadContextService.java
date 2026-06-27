package com.samara.emailsummary.service;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.provider.EmailProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadContextService {

    private final EmailProvider emailProvider;

    public ThreadContextService(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    public List<EmailDetalheDTO> recuperarContextoPorThread(String threadId) {
        if (threadId == null || threadId.isBlank()) {
            return List.of();
        }

        return emailProvider.buscarEmailsPorThreadId(threadId);
    }
}