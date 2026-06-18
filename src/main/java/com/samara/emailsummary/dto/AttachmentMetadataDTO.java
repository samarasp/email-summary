package com.samara.emailsummary.dto;

public class AttachmentMetadataDTO {

    private String nomeArquivo;
    private String mimeType;
    private Long tamanho;
    private String attachmentId;

    public AttachmentMetadataDTO() {
    }

    public AttachmentMetadataDTO(String nomeArquivo,
                                 String mimeType,
                                 Long tamanho,
                                 String attachmentId) {
        this.nomeArquivo = nomeArquivo;
        this.mimeType = mimeType;
        this.tamanho = tamanho;
        this.attachmentId = attachmentId;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public String getAttachmentId() {
        return attachmentId;
    }
}
