package com.samara.emailsummary.service;

public interface EmailSenderService {

    void enviarResumo(String destinatario, String assunto, String corpo);
}