package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.briefing.dto.BriefingAnalysis;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.mapper.BriefingMapper;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.service.EmailSenderService;
import com.samara.emailsummary.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyBriefingEmailService {

    private static final Logger log =
            LoggerFactory.getLogger(DailyBriefingEmailService.class);

    private final DailyBriefingFormatterService dailyBriefingFormatterService;
    private final EmailSenderService emailSenderService;
    private final BriefingAnalysisService briefingAnalysisService;
    private final BriefingMapper briefingMapper;

    public DailyBriefingEmailService(
            DailyBriefingFormatterService dailyBriefingFormatterService,
            EmailSenderService emailSenderService,
            BriefingAnalysisService briefingAnalysisService,
            BriefingMapper briefingMapper
    ) {
        this.dailyBriefingFormatterService = dailyBriefingFormatterService;
        this.emailSenderService = emailSenderService;
        this.briefingAnalysisService = briefingAnalysisService;
        this.briefingMapper = briefingMapper;
    }

    public void enviarBriefingDiario(EmailService emailService, String destination) {

        List<EmailResumoDTO> emails = emailService.listarEmails();

        Map<String, EmailDetalheDTO> emailsPorConversa = new LinkedHashMap<>();

        for (EmailResumoDTO emailResumo : emails) {

            try {

                EmailDetalheDTO email =
                        emailService.buscarEmailPorId(emailResumo.getId());

                if (deveIgnorarEmail(email.getAssunto())) {
                    continue;
                }

                String chaveConversa = obterChaveConversa(email);

                emailsPorConversa.putIfAbsent(chaveConversa, email);

            } catch (Exception e) {

                log.warn(
                        "Falha ao buscar um e-mail no Daily Briefing. O processamento continuará.",
                        e
                );
            }
        }

        List<EmailDetalheDTO> emailsDetalhados =
                new ArrayList<>(emailsPorConversa.values());

        BriefingAnalysis analysis =
                briefingAnalysisService.gerarAnalise(emailsDetalhados);

        DailyBriefing briefing =
                briefingMapper.toDailyBriefing(analysis);

        String corpo =
                dailyBriefingFormatterService.formatar(briefing);

        String data =
                LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        emailSenderService.enviarResumo(
                destination,
                "Daily Briefing - " + data,
                corpo
        );
    }

    private String obterChaveConversa(EmailDetalheDTO email) {

        if (email.getThreadId() != null && !email.getThreadId().isBlank()) {
            return email.getThreadId();
        }

        if (email.getAssunto() != null && !email.getAssunto().isBlank()) {
            return email.getAssunto();
        }

        return email.getId();
    }

    private boolean deveIgnorarEmail(String assunto) {

        if (assunto == null || assunto.isBlank()) {
            return false;
        }

        String assuntoNormalizado =
                assunto.trim().toLowerCase();

        return assuntoNormalizado.startsWith("resumo do e-mail:")
                || assuntoNormalizado.startsWith("daily briefing")
                || assuntoNormalizado.startsWith("briefing da manhã");
    }
}