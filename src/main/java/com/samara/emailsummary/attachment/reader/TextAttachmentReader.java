package com.samara.emailsummary.attachment.reader;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class TextAttachmentReader implements AttachmentReader {

    @Override
    public boolean suporta(String mimeType) {
        return "text/plain".equalsIgnoreCase(mimeType);
    }

    @Override
    public String extrairTexto(byte[] arquivo) {

        return new String(arquivo, StandardCharsets.UTF_8).trim();
    }
}