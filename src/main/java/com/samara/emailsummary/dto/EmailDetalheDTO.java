package com.samara.emailsummary.dto;

public class EmailDetalheDTO {

    private String id;
    private String remetente;
    private String assunto;
    private String corpo;
    private String data;
    private boolean temAnexo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}