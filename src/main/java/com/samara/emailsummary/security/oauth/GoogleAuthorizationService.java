package com.samara.emailsummary.security.oauth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class GoogleAuthorizationService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String CREDENTIALS_FILE_PATH = "/google/credentials.json";

    private static final String TOKENS_DIRECTORY_PATH = "data/tokens";

    private static final String EXTERNAL_CREDENTIALS_FILE_PATH = "config/credentials.json";

    private InputStream localizarCredentials() throws IOException {

        File externalCredentials = new File(EXTERNAL_CREDENTIALS_FILE_PATH);

        if (externalCredentials.exists() && externalCredentials.isFile()) {
            return new FileInputStream(externalCredentials);
        }

        InputStream inputStream = getClass()
                .getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (inputStream == null) {
            throw new IOException("Arquivo credentials.json não encontrado.");
        }

        return inputStream;
    }

    private GoogleClientSecrets loadClientSecrets() throws IOException {

        InputStream inputStream = localizarCredentials();

        return GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(inputStream)
        );
    }

    private GoogleAuthorizationCodeFlow createAuthorizationFlow(
            GoogleClientSecrets clientSecrets,
            String mailboxId
    ) throws Exception {

        File tokensDirectory = new File(TOKENS_DIRECTORY_PATH, mailboxId);

        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                List.of(
                        GmailScopes.GMAIL_READONLY,
                        GmailScopes.GMAIL_SEND
                )
        )
                .setDataStoreFactory(new FileDataStoreFactory(tokensDirectory))
                .setAccessType("offline")
                .build();
    }

    private Credential authorizeUser(
            GoogleAuthorizationCodeFlow flow,
            String mailboxId
    ) throws Exception {

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize(mailboxId);
    }

    public Credential authorize() throws Exception {
        return authorize("default");
    }

    public Credential authorize(String mailboxId) throws Exception {

        GoogleClientSecrets clientSecrets = loadClientSecrets();

        GoogleAuthorizationCodeFlow flow =
                createAuthorizationFlow(clientSecrets, mailboxId);

        return authorizeUser(flow, mailboxId);
    }
}