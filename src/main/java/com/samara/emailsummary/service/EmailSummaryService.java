package com.samara.emailsummary.service;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.service.SummaryService;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.ai.dto.AnalysisType;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailSummaryService {

    @Value("${email.summary.destination}")
    private String destination;

    private final EmailService emailService;
    private final SummaryService summaryService;
    private final EmailSummaryFormatterService formatterService;
    private final EmailSenderService emailSenderService;

    public EmailSummaryService(
            EmailService emailService,
            SummaryService summaryService,
            EmailSummaryFormatterService formatterService,
            EmailSenderService emailSenderService
    ) {
        this.emailService = emailService;
        this.summaryService = summaryService;
        this.formatterService = formatterService;
        this.emailSenderService = emailSenderService;
    }

    public void enviarResumoPorEmail(String emailId) {

        EmailDetalheDTO email = emailService.buscarEmailPorId(emailId);

        SummaryRequest request = new SummaryRequest(
                AnalysisType.EMAIL_SUMMARY,
                email.getAssunto(),
                email.getRemetente(),
                email.getCorpo()
        );

        SummaryResponse resumo = summaryService.gerarResumo(request);

        String corpoFormatado = formatterService.formatar(resumo);

        String assunto = "Resumo do e-mail: " + email.getAssunto();

        emailSenderService.enviarResumo(destination, assunto, corpoFormatado);
    }
}