package com.samara.emailsummary.attachment.service;

import com.samara.emailsummary.attachment.reader.AttachmentReader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentProcessingService {

    private final List<AttachmentReader> readers;

    public AttachmentProcessingService(List<AttachmentReader> readers) {
        this.readers = readers;
    }

    public String extrairTexto(String mimeType, byte[] arquivo) throws Exception {

        AttachmentReader reader = readers.stream()
                .filter(r -> r.suporta(mimeType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de anexo não suportado: " + mimeType));

        return reader.extrairTexto(arquivo);
    }

}