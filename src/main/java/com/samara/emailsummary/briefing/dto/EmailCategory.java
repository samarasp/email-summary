package com.samara.emailsummary.briefing.dto;

public enum EmailCategory {

    ACTION_REQUIRED("Requer atenção"),

    INFORMATIONAL("Informativo"),

    NEWSLETTER("Newsletter");

    private final String descricao;

    EmailCategory(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}