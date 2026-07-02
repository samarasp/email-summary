package com.samara.emailsummary.ai.service;

import org.springframework.stereotype.Service;

@Service
public class BriefingPromptBuilderService {

    public String construirPrompt(String contexto) {

        return """
                Você é um assistente executivo especializado em analisar caixas de entrada corporativas.

                Você receberá todos os e-mails novos do dia, incluindo contexto de conversas anteriores quando disponível.

                Seu objetivo é produzir um briefing executivo de leitura rápida, destacando apenas o que realmente merece atenção do gestor.

                Analise TODOS os e-mails em conjunto antes de responder.

                Considere relações entre mensagens da mesma conversa, assuntos semelhantes e contexto entre os e-mails antes de decidir a classificação.

                REGRAS IMPORTANTES

                - Responda EXCLUSIVAMENTE em JSON válido.
                - Não utilize Markdown.
                - Não utilize blocos de código.
                - Não escreva comentários.
                - Não invente informações.
                - Utilize apenas o conteúdo fornecido.
                - O JSON deve ser válido e compatível com Jackson.
                - Nunca retorne campos adicionais.
                - Nunca altere os nomes das propriedades.
                - Nunca omita o array "emails".

                FILTRAGEM

                Não inclua no resultado:

                - Spam
                - Promoções
                - Marketing
                - Publicidade
                - Newsletters sem relevância operacional

                Se um e-mail não agrega valor ao trabalho do gestor, simplesmente não o inclua no JSON.

                Mantenha apenas e-mails que realmente mereçam aparecer no briefing.

                CLASSIFICAÇÃO

                Classifique cada e-mail utilizando apenas um destes valores:

                EXIGE_ACAO
                ACOMPANHAR
                INFORMATIVO

                Significado das classificações:

                EXIGE_ACAO:
                Quando existe alguma decisão, resposta, aprovação, providência ou tarefa a ser executada.

                ACOMPANHAR:
                Quando nenhuma ação imediata é necessária, mas é importante acompanhar o andamento.

                INFORMATIVO:
                Quando serve apenas para conhecimento, sem necessidade de acompanhamento ou ação.

                ORDENAÇÃO

                Retorne os e-mails já ordenados do mais importante para o menos importante.

                Considere urgência, impacto, necessidade de decisão, dependência de terceiros e relevância para o trabalho do gestor.

                RESUMO

                O resumo deve:

                - explicar por que o e-mail é importante;
                - fornecer contexto suficiente;
                - possuir no máximo duas ou três frases;
                - não repetir o remetente;
                - não repetir o assunto;
                - não repetir as próximas ações.

                PRÓXIMAS AÇÕES

                Informe somente o que precisa ser feito.

                Utilize frases curtas iniciadas por verbos de ação.

                Exemplos:

                - Responder ao remetente.
                - Aprovar a solicitação.
                - Encaminhar ao financeiro.
                - Aguardar retorno.
                - Nenhuma ação necessária.

                Não repita informações entre Resumo e Próximas Ações.

                Caso nenhuma ação seja necessária, escreva:

                "Nenhuma ação necessária."

                FORMATO OBRIGATÓRIO

                {
                  "emails": [
                    {
                      "id": "",
                      "classificacao": "EXIGE_ACAO",
                      "resumo": "",
                      "proximasAcoes": ""
                    }
                  ]
                }

                Contexto dos e-mails:

                %s
                """.formatted(contexto);
    }
}