package com.samara.emailsummary.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.samara.emailsummary.security.oauth.GoogleAuthorizationService;
import org.springframework.stereotype.Service;

@Service
public class GmailClientFactory {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "Email Summary";

    private final GoogleAuthorizationService authorizationService;

    public GmailClientFactory(GoogleAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public Gmail criarCliente(String mailboxId) throws Exception {

        Credential credential = authorizationService.authorize(mailboxId);

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        )
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}