package com.samara.emailsummary.scheduler;

import com.samara.emailsummary.briefing.service.MultiMailboxBriefingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailSummaryScheduler {

    private final MultiMailboxBriefingService multiMailboxBriefingService;

    public EmailSummaryScheduler(
            MultiMailboxBriefingService multiMailboxBriefingService
    ) {
        this.multiMailboxBriefingService = multiMailboxBriefingService;
    }

    @Scheduled(cron = "0 0 8 * * *", zone = "America/Sao_Paulo")
    public void executarResumoDiario() {
        multiMailboxBriefingService.enviarBriefings();
    }
}