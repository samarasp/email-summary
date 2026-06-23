package com.samara.emailsummary.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class GmailEmailSenderService implements EmailSenderService {

    private final Gmail gmail;

    public GmailEmailSenderService(Gmail gmail) {
        this.gmail = gmail;
    }

    @Override
    public void enviarResumo(String destinatario, String assunto, String corpo) {
        try {
            MimeMessage email = criarEmail(destinatario, assunto, corpo);
            Message mensagem = criarMensagem(email);

            gmail.users()
                    .messages()
                    .send("me", mensagem)
                    .execute();

        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Erro ao enviar e-mail de resumo.", e);
        }
    }

    private MimeMessage criarEmail(String destinatario, String assunto, String corpo)
            throws MessagingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.addRecipient(
                jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(destinatario)
        );
        email.setSubject(assunto, "UTF-8");
        email.setText(corpo, "UTF-8");

        return email;
    }

    private Message criarMensagem(MimeMessage email)
            throws MessagingException, IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);

        String emailCodificado = Base64.encodeBase64URLSafeString(buffer.toByteArray());

        Message mensagem = new Message();
        mensagem.setRaw(emailCodificado);

        return mensagem;
    }
}