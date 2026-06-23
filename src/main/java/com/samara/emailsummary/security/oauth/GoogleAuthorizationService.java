package com.samara.emailsummary.security.oauth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

import java.util.List;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

@Service
public class GoogleAuthorizationService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String CREDENTIALS_FILE_PATH = "/google/credentials.json";

    private static final String TOKENS_DIRECTORY_PATH = "data/tokens";

    private GoogleClientSecrets loadClientSecrets() throws IOException {

        InputStream inputStream = getClass()
                .getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (inputStream == null) {
            throw new IOException("Arquivo credentials.json não encontrado.");
        }

        return GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(inputStream)
        );
    }

    private GoogleAuthorizationCodeFlow createAuthorizationFlow(
            GoogleClientSecrets clientSecrets) throws Exception {

        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                List.of(
                        GmailScopes.GMAIL_READONLY,
                        GmailScopes.GMAIL_SEND
                )
        )
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }

    private Credential authorizeUser(
            GoogleAuthorizationCodeFlow flow) throws Exception {

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("diretoria@sbot.org.br");
    }

    public Credential authorize() throws Exception {

        GoogleClientSecrets clientSecrets = loadClientSecrets();

        GoogleAuthorizationCodeFlow flow = createAuthorizationFlow(clientSecrets);

        return authorizeUser(flow);
    }
}