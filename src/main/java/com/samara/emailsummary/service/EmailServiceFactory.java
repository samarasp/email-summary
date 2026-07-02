package com.samara.emailsummary.service;

import com.google.api.services.gmail.Gmail;
import com.samara.emailsummary.attachment.service.AttachmentProcessingService;
import com.samara.emailsummary.attachment.service.AttachmentValidationService;
import com.samara.emailsummary.config.EmailSummaryProperties;
import com.samara.emailsummary.provider.GmailProvider;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceFactory {

    private final AttachmentValidationService attachmentValidationService;
    private final AttachmentProcessingService attachmentProcessingService;
    private final EmailSummaryProperties emailSummaryProperties;

    public EmailServiceFactory(AttachmentValidationService attachmentValidationService,
                               AttachmentProcessingService attachmentProcessingService,
                               EmailSummaryProperties emailSummaryProperties) {
        this.attachmentValidationService = attachmentValidationService;
        this.attachmentProcessingService = attachmentProcessingService;
        this.emailSummaryProperties = emailSummaryProperties;
    }

    public EmailService criar(Gmail gmail) {
        GmailProvider provider = new GmailProvider(
                gmail,
                attachmentValidationService,
                attachmentProcessingService,
                emailSummaryProperties
        );

        return new EmailService(provider);
    }
}