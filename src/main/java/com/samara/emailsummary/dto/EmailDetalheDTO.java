package com.samara.emailsummary.dto;

import java.util.List;

public class EmailDetalheDTO {

    private String id;
    private String threadId;
    private String remetente;
    private String assunto;
    private String corpo;
    private String data;
    private boolean temAnexo;
    private List<AttachmentMetadataDTO> anexos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isTemAnexo() {
        return temAnexo;
    }

    public void setTemAnexo(boolean temAnexo) {
        this.temAnexo = temAnexo;
    }

    public List<AttachmentMetadataDTO> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<AttachmentMetadataDTO> anexos) {
        this.anexos = anexos;
    }
}