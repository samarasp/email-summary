package com.samara.emailsummary.scheduler;

import com.samara.emailsummary.briefing.service.DailyBriefingEmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailSummaryScheduler {

    private final DailyBriefingEmailService dailyBriefingEmailService;

    public EmailSummaryScheduler(DailyBriefingEmailService dailyBriefingEmailService) {
        this.dailyBriefingEmailService = dailyBriefingEmailService;
    }

    @Scheduled(cron = "0 0 8 * * *", zone = "America/Sao_Paulo")
    public void executarResumoDiario() {
        dailyBriefingEmailService.enviarBriefingDiario();
    }
}