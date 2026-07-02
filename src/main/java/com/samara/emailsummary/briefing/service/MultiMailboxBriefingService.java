package com.samara.emailsummary.briefing.service;

import com.google.api.services.gmail.Gmail;
import com.samara.emailsummary.config.GmailAccountsProperties;
import com.samara.emailsummary.config.MailboxProperties;
import com.samara.emailsummary.service.EmailService;
import com.samara.emailsummary.service.EmailServiceFactory;
import com.samara.emailsummary.service.GmailClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MultiMailboxBriefingService {

    private static final Logger log =
            LoggerFactory.getLogger(MultiMailboxBriefingService.class);

    private final GmailAccountsProperties gmailAccountsProperties;
    private final GmailClientFactory gmailClientFactory;
    private final EmailServiceFactory emailServiceFactory;
    private final DailyBriefingEmailService dailyBriefingEmailService;

    public MultiMailboxBriefingService(
            GmailAccountsProperties gmailAccountsProperties,
            GmailClientFactory gmailClientFactory,
            EmailServiceFactory emailServiceFactory,
            DailyBriefingEmailService dailyBriefingEmailService
    ) {
        this.gmailAccountsProperties = gmailAccountsProperties;
        this.gmailClientFactory = gmailClientFactory;
        this.emailServiceFactory = emailServiceFactory;
        this.dailyBriefingEmailService = dailyBriefingEmailService;
    }

    public void enviarBriefings() {

        for (String mailboxId : gmailAccountsProperties.getMailboxes()) {

            MailboxProperties mailbox =
                    gmailAccountsProperties.getMailbox().get(mailboxId);

            if (mailbox == null || !mailbox.isEnabled() || !mailbox.isSummaryEnabled()) {
                continue;
            }

            try {
                log.info("Gerando briefing da mailbox: {}", mailboxId);

                Gmail gmail = gmailClientFactory.criarCliente(mailboxId);

                EmailService emailService =
                        emailServiceFactory.criar(gmail);

                dailyBriefingEmailService.enviarBriefingDiario(
                        emailService,
                        mailbox.getDestination()
                );

                log.info("Briefing enviado com sucesso para mailbox: {}", mailboxId);

            } catch (Exception e) {
                log.error("Erro ao processar mailbox: {}", mailboxId, e);
            }
        }
    }
}