package com.samara.emailsummary.attachment.service;

import org.springframework.stereotype.Service;
import com.samara.emailsummary.dto.AttachmentMetadataDTO;

import java.util.Set;

@Service
public class AttachmentValidationService {

    private static final long TAMANHO_MAXIMO_BYTES = 10 * 1024 * 1024;

    private static final Set<String> MIME_TYPES_PERMITIDOS = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
    );
    public void validar(AttachmentMetadataDTO anexo) {

        if (anexo.getNomeArquivo() == null || anexo.getNomeArquivo().isBlank()) {
            throw new IllegalArgumentException("Nome do anexo inválido.");
        }

        if (anexo.getMimeType() == null || !MIME_TYPES_PERMITIDOS.contains(anexo.getMimeType())) {
            throw new IllegalArgumentException("Tipo de anexo não permitido: " + anexo.getMimeType());
        }

        if (anexo.getTamanho() == null || anexo.getTamanho() <= 0) {
            throw new IllegalArgumentException("Anexo vazio ou tamanho inválido.");
        }

        if (anexo.getTamanho() > TAMANHO_MAXIMO_BYTES) {
            throw new IllegalArgumentException("Anexo excede o tamanho máximo permitido.");
        }
    }
}