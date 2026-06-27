package com.samara.emailsummary.briefing.dto;

public enum EmailCategory {

    CRITICAL("Crítico", 100),

    HIGH_PRIORITY("Alta prioridade", 80),

    ACTION_REQUIRED("Requer atenção", 60),

    INFORMATIONAL("Informativo", 30),

    NEWSLETTER("Newsletter", 0);

    private final String descricao;
    private final int peso;

    EmailCategory(String descricao, int peso) {
        this.descricao = descricao;
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPeso() {
        return peso;
    }
}