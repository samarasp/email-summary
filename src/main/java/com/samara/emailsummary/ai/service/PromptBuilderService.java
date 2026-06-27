package com.samara.emailsummary.ai.service;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    public String construirPrompt(SummaryRequest request) {
        return switch (request.analysisType()) {
            case EMAIL_SUMMARY -> construirPromptEmail(request);
            case DAILY_BRIEFING -> construirPromptBriefing(request);
        };
    }

    private String construirPromptEmail(SummaryRequest request) {
        return """
            Você é um assistente especializado em análise de e-mails institucionais.

            Analise cuidadosamente o e-mail abaixo.

            Responda EXCLUSIVAMENTE em JSON válido.
            Não use markdown.
            Não use bloco de código.
            Não escreva explicações fora do JSON.
            Não invente informações.
            Use apenas o conteúdo fornecido.

            O JSON deve seguir exatamente esta estrutura:

            {
              "resumo": "",
              "prioridade": "",
              "acoesSugeridas": [],
              "pendencias": [],
              "prazos": [],
              "pessoasCitadas": [],
              "necessitaResposta": false,
              "sugestaoResposta": "",
              "nivelConfianca": ""
            }

            Regras para preenchimento:
            - resumo: resumo executivo claro, objetivo e profissional.
            - prioridade: use apenas "Baixa", "Média", "Alta" ou "Crítica".
            - acoesSugeridas: lista de ações recomendadas.
            - pendencias: lista de pendências identificadas.
            - prazos: lista de prazos mencionados.
            - pessoasCitadas: liste apenas pessoas, instituições, comissões ou áreas relevantes para decisão, ação, pendência ou contexto institucional; ignore nomes usados apenas em saudações ou cumprimentos.
            - necessitaResposta: true se o e-mail exigir resposta; caso contrário, false.
            - sugestaoResposta: preencha apenas se necessitaResposta for true; caso contrário, use string vazia.
            - nivelConfianca: use apenas "Baixa", "Média" ou "Alta".

            Dados do e-mail:

            Remetente:
            %s

            Assunto:
            %s

            Conteúdo:
            %s
            """.formatted(
                request.remetente(),
                request.assunto(),
                request.conteudo()
        );
    }

    private String construirPromptBriefing(SummaryRequest request) {
        return """
            Você é um assistente executivo especializado em análise de e-mails institucionais.

            Você receberá um conjunto de e-mails do dia.

            Seu objetivo NÃO é resumir cada e-mail individualmente.
            Seu objetivo é produzir um Briefing Executivo Diário consolidado, claro e útil para tomada de decisão.

            Analise todos os e-mails em conjunto.

            Durante a análise:
            - identifique os assuntos mais importantes do dia;
            - destaque decisões que exigem atenção;
            - identifique pendências;
            - identifique prazos;
            - identifique compromissos, reuniões ou eventos mencionados;
            - identifique pessoas e instituições relevantes;
            - agrupe assuntos relacionados;
            - elimine repetições;
            - trate newsletters e comunicados informativos como informações secundárias;
            - destaque riscos, urgências ou pontos sensíveis;
            - use apenas o conteúdo fornecido;
            - não expanda siglas nem deduza nomes de instituições;
            - em pessoasCitadas, liste apenas pessoas, instituições, comissões ou áreas relevantes para tomada de decisão;
            - ignore apelidos, saudações e nomes citados apenas em cumprimentos, salvo se forem relevantes para uma ação ou pendência;
            - não invente informações.

            Responda EXCLUSIVAMENTE em JSON válido.
            Não use markdown.
            Não use bloco de código.
            Não escreva explicações fora do JSON.

            O JSON deve seguir exatamente esta estrutura:

            {
              "resumo": "",
              "prioridade": "",
              "acoesSugeridas": [],
              "pendencias": [],
              "prazos": [],
              "pessoasCitadas": [],
              "necessitaResposta": false,
              "sugestaoResposta": "",
              "nivelConfianca": ""
            }

            Regras para preenchimento:
            - resumo: escreva uma visão executiva consolidada do dia, destacando os principais temas e prioridades.
            - prioridade: use apenas "Baixa", "Média", "Alta" ou "Crítica", considerando o conjunto dos e-mails.
            - acoesSugeridas: liste ações práticas recomendadas para o dia.
            - pendencias: liste pendências identificadas no conjunto de e-mails.
            - prazos: liste prazos, datas, horários, reuniões ou eventos mencionados.
            - pessoasCitadas: liste pessoas, instituições, comissões ou áreas relevantes citadas.
            - necessitaResposta: true se algum item do briefing exigir resposta ou encaminhamento; caso contrário, false.
            - sugestaoResposta: se houver uma resposta geral útil, escreva uma sugestão breve; caso contrário, use string vazia.
            - nivelConfianca: use apenas "Baixa", "Média" ou "Alta".

            Dados do briefing:

            Assunto:
            %s

            Origem:
            %s

            Conteúdo consolidado:
            %s
            """.formatted(
                request.assunto(),
                request.remetente(),
                request.conteudo()
        );
    }
}