# Email Summary

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

## Sobre o projeto

O **Email Summary** é uma aplicação backend desenvolvida em **Java 21** e **Spring Boot** que automatiza o processamento de e-mails corporativos utilizando Inteligência Artificial.

A solução integra-se à **Gmail API** por meio de **OAuth 2.0**, realiza a leitura segura das mensagens, processa automaticamente seus anexos e utiliza o **Google Gemini** para transformar o conteúdo dos e-mails em informações estruturadas e acionáveis.

Além da geração de resumos executivos, a aplicação é capaz de identificar prioridades, destacar pendências, sugerir ações, detectar a necessidade de resposta, elaborar sugestões de resposta e consolidar essas informações em um **Daily Briefing**, permitindo uma visão rápida dos principais assuntos recebidos.

O sistema também pode enviar automaticamente os resumos gerados por e-mail utilizando a própria Gmail API, criando um fluxo completo de automação para acompanhamento de mensagens corporativas.

Desde o início do desenvolvimento, o projeto foi concebido com foco em:

* Arquitetura em camadas;
* Baixo acoplamento;
* Princípios SOLID;
* Responsabilidade Única (SRP);
* Segurança no gerenciamento de credenciais;
* Facilidade de manutenção e evolução;
* Componentes reutilizáveis;
* Preparação para suportar múltiplos provedores de e-mail e de Inteligência Artificial.

---

## 🔄 Fluxo da Aplicação

```text
                 Gmail API
                     │
                     ▼
           Leitura dos e-mails
                     │
                     ▼
          Pré-processamento do texto
                     │
                     ▼
        Processamento de anexos (PDF/DOCX/TXT)
                     │
                     ▼
              Google Gemini
                     │
                     ▼
          SummaryResponse estruturado
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
Resumo formatado         Daily Briefing
          │
          ▼
Envio automático por e-mail
```

# ✨ Funcionalidades

Atualmente, a aplicação oferece os seguintes recursos:

## 📧 Leitura de e-mails

* ✅ Autenticação segura utilizando OAuth 2.0 do Google;
* ✅ Integração com a Gmail API;
* ✅ Listagem dos e-mails mais recentes;
* ✅ Consulta detalhada de um e-mail pelo ID;
* ✅ Extração automática de:

  * remetente;
  * assunto;
  * data;
  * corpo da mensagem;
  * indicação de anexos.

## 📎 Processamento de anexos

* ✅ Leitura de arquivos PDF;
* ✅ Leitura de documentos DOCX;
* ✅ Leitura de arquivos TXT;
* ✅ Validação do tipo, tamanho e nome dos arquivos;
* ✅ Incorporação automática do conteúdo dos anexos ao texto do e-mail antes da análise pela IA.

## 🧹 Preparação do conteúdo

Antes do envio para a Inteligência Artificial, o conteúdo é tratado automaticamente por meio de:

* ✅ Remoção de assinaturas;
* ✅ Remoção de históricos de conversas;
* ✅ Remoção de encaminhamentos;
* ✅ Normalização do texto para melhorar a qualidade da análise.

## 🤖 Inteligência Artificial

A integração com o Google Gemini permite gerar automaticamente:

* ✅ Resumo executivo;
* ✅ Classificação de prioridade;
* ✅ Pendências;
* ✅ Ações sugeridas;
* ✅ Prazos identificados;
* ✅ Pessoas citadas;
* ✅ Necessidade de resposta;
* ✅ Sugestão de resposta;
* ✅ Nível de confiança da análise.

A resposta é retornada em formato JSON e convertida automaticamente para objetos Java utilizando **Jackson (`ObjectMapper`)**.

## 🌐 API REST

Atualmente, a aplicação disponibiliza os seguintes recursos por meio de uma API REST:

### 📧 Gerenciamento de e-mails

* ✅ Listagem dos e-mails mais recentes;
* ✅ Consulta detalhada de um e-mail específico;
* ✅ Geração de resumo executivo utilizando Inteligência Artificial.

### ✉️ Envio automático de resumos

Após gerar o resumo com o Google Gemini, a aplicação pode enviá-lo automaticamente por e-mail utilizando a Gmail API.

### 📋 Daily Briefing

Além dos resumos individuais, a aplicação pode gerar um **Daily Briefing**, consolidando os principais e-mails processados em um único relatório organizado por prioridade.


## 🧠 Classificação Inteligente

Antes do envio de um e-mail para a Inteligência Artificial, a aplicação realiza uma classificação inicial para identificar mensagens com características de newsletters ou conteúdos meramente informativos.

Essa etapa permite:

* reduzir chamadas desnecessárias ao modelo de IA;
* melhorar o desempenho da aplicação;
* diminuir custos de processamento;
* tornar o Daily Briefing mais relevante.

A classificação foi implementada de forma extensível, permitindo incorporar novos critérios e categorias nas próximas versões do projeto.


# 🏗️ Arquitetura

O projeto foi desenvolvido seguindo uma arquitetura em camadas, com foco em baixo acoplamento, responsabilidade única (SRP) e facilidade de manutenção.

Cada componente possui uma responsabilidade bem definida, tornando a aplicação extensível e preparada para futuras evoluções.

## Fluxo de leitura dos e-mails

```text
Controller
    │
    ▼
Service
    │
    ▼
EmailProvider
    │
    ▼
GmailProvider
    │
    ▼
Gmail API
```

## Fluxo da Inteligência Artificial

```text
Controller
    │
    ▼
SummaryService
    │
    ▼
PromptBuilderService
    │
    ▼
AiProvider
    │
    ▼
GeminiProvider
    │
    ▼
GeminiClient
    │
    ▼
Google Gemini
    │
    ▼
GeminiResponseParser
    │
    ▼
SummaryResponse
```

## Fluxo de processamento de anexos

```text
AttachmentValidationService
            │
            ▼
AttachmentProcessingService
            │
            ▼
AttachmentReader
      ├── PdfAttachmentReader
      ├── DocxAttachmentReader
      └── TextAttachmentReader
```

Essa organização permite substituir ou adicionar novos provedores de IA, serviços de e-mail ou leitores de anexos com impacto mínimo no restante da aplicação.

---

# 🛠️ Tecnologias Utilizadas

| Categoria            | Tecnologia        |
| -------------------- | ----------------- |
| Linguagem            | Java 21           |
| Framework            | Spring Boot 3.5   |
| Build                | Maven             |
| IA                   | Google Gemini     |
| E-mail               | Gmail API         |
| Autenticação         | OAuth 2.0         |
| JSON                 | Jackson           |
| Documentos           | Apache PDFBox     |
| Documentos Word      | Apache POI        |
| Testes               | JUnit 5 + Mockito |
| Versionamento        | Git               |
| Hospedagem do código | GitHub            |

---

# 📂 Estrutura do Projeto

```text
src
├── ai
│   ├── client
│   ├── dto
│   ├── exception
│   ├── parser
│   ├── provider
│   └── service
│
├── attachment
│   ├── reader
│   ├── service
│   └── validator
│
├── briefing
│   ├── dto
│   └── service
│
├── config
├── controller
├── dto
├── provider
├── security
└── service
```

A organização dos pacotes segue responsabilidades bem definidas, facilitando a manutenção, os testes e a evolução do sistema.

# 🔒 Segurança

A segurança foi tratada como um requisito desde o início do desenvolvimento do projeto.

Entre as principais medidas adotadas estão:

## Credenciais

* ✅ OAuth 2.0 para autenticação com a Gmail API;
* ✅ Escopos Gmail READONLY e GMAIL_SEND;
* ✅ API Key do Google Gemini armazenada em variável de ambiente;
* ✅ `credentials.json` mantido fora do controle de versão;
* ✅ `.gitignore` configurado para impedir o versionamento de arquivos sensíveis.

## Proteção de informações

Os logs da aplicação foram projetados para evitar a exposição de dados sensíveis. Não são registrados:

* conteúdo dos e-mails;
* conteúdo dos anexos;
* API Keys;
* OAuth Tokens;
* JWTs;
* credenciais.

## Arquitetura segura

A organização do projeto segue princípios que favorecem a manutenção e reduzem riscos durante a evolução do código:

* baixo acoplamento;
* responsabilidade única (SRP);
* princípios SOLID;
* separação entre camadas;
* componentes especializados e reutilizáveis.


# ✅ Qualidade do Código

Durante o desenvolvimento, o projeto passou por um processo contínuo de refatoração para manter uma arquitetura limpa e de fácil manutenção.

Entre as melhorias implementadas estão:

* criação do `GeminiResponseParser` para centralizar a desserialização das respostas da IA;
* separação das responsabilidades entre Provider, Client e Parser;
* tratamento específico para `IOException`, `JsonProcessingException` e `InterruptedException`;
* revisão completa da estratégia de logs;
* padronização da comunicação entre os componentes da aplicação.

Essa organização torna o sistema mais simples de testar, evoluir e manter.


# 🧪 Testes

O projeto possui testes unitários para os principais componentes da integração com a Inteligência Artificial.

Atualmente estão implementados testes para:

### GeminiResponseParser

* JSON válido;
* JSON com bloco Markdown;
* JSON inválido;
* resposta nula.

### PromptBuilderService

* construção correta do prompt.

### GeminiProvider

* fluxo de sucesso;
* tratamento de `IOException`;
* tratamento de `JsonProcessingException`;
* tratamento de `InterruptedException`.

Além dos testes específicos, a aplicação também possui teste de carregamento do contexto do Spring Boot.

**Resultado atual:**

* ✅ 10 testes implementados;
* ✅ Todos os testes passando.

# ▶️ Como Executar

## Pré-requisitos

* Java 21
* Maven 3.9+
* Conta Google com acesso à Gmail API
* Projeto configurado no Google Cloud
* API Key do Google Gemini

## Configuração

1. Defina a chave da API do Gemini como variável de ambiente:

```bash
GEMINI_API_KEY=sua_chave_aqui
```

2. Adicione o arquivo `credentials.json` (OAuth 2.0) no local configurado pela aplicação.

3. Na primeira execução, autorize o acesso à sua conta Google. O token OAuth será gerado automaticamente.

> **Importante**
>
> Nunca versione:
>
> * `credentials.json`;
> * a pasta `data/tokens`;
> * chaves de API;
> * tokens OAuth;
> * qualquer outra credencial da aplicação.

## Executando

```bash
mvn spring-boot:run
```

Após a inicialização, a aplicação estará disponível em:

```text
http://localhost:8080
```


---

# 📌 Endpoints

| Método | Endpoint                     | Descrição                                        |
| ------ | ---------------------------- | ------------------------------------------------ |
| GET    | `/emails`                    | Lista os e-mails mais recentes                   |
| GET    | `/emails/{id}`               | Retorna os detalhes de um e-mail                 |
| GET    | `/emails/{id}/resumo`        | Gera um resumo utilizando IA                     |
| POST   | `/emails/{id}/resumo/enviar` | Gera e envia automaticamente o resumo por e-mail |
| GET    | `/emails/briefing`           | Gera o Daily Briefing                            |

# 📋 Daily Briefing

Além dos resumos individuais, a aplicação pode gerar um **Daily Briefing**, consolidando os principais e-mails processados em um único relatório organizado por prioridade.

O briefing apresenta:

* quantidade de e-mails analisados;
* distribuição por prioridade;
* pendências;
* ações sugeridas;
* pessoas citadas;
* necessidade de resposta;
* nível de confiança da IA.

Esse recurso foi desenvolvido para oferecer uma visão rápida das principais demandas do dia, reduzindo o tempo gasto na triagem da caixa de entrada.

# 🗺️ Roadmap

As próximas evoluções previstas para o projeto incluem:

* execução automática dos resumos utilizando `@Scheduled`;
* configuração de múltiplos destinatários;
* integração com Microsoft Outlook;
* processamento de imagens em anexos (OCR);
* dashboard web para consulta dos resumos e briefings;
* suporte a novos provedores de Inteligência Artificial.

---

# 🤝 Contribuição

Contribuições são bem-vindas.

Caso encontre algum problema ou tenha sugestões de melhoria, fique à vontade para abrir uma *Issue* ou enviar um *Pull Request*.

---

# 👩‍💻 Sobre a autora

Desenvolvido por **Samara Silva**, estudante de Análise e Desenvolvimento de Sistemas, com foco em desenvolvimento backend, arquitetura de software, APIs REST e integração com Inteligência Artificial.

Este projeto foi criado como parte da construção do meu portfólio, aplicando boas práticas de engenharia de software, princípios SOLID, segurança, testes automatizados e integração com serviços do Google.
