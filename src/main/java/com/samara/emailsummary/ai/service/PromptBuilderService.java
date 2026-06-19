package com.samara.emailsummary.ai.service;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    public String construirPrompt(SummaryRequest request) {
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
            - pessoasCitadas: lista de pessoas citadas no e-mail.
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
}