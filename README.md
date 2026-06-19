# Email Summary

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

## Sobre o projeto

O **Email Summary** é uma aplicação backend desenvolvida em **Java 21** e **Spring Boot** para automatizar a leitura de e-mails corporativos e gerar resumos executivos utilizando Inteligência Artificial.

A aplicação integra-se à **Gmail API** por meio de **OAuth 2.0**, processa o conteúdo das mensagens e de seus anexos e utiliza o **Google Gemini** para produzir análises estruturadas, incluindo resumo, prioridade, pendências, ações sugeridas, prazos, pessoas citadas, necessidade de resposta e sugestão de resposta.

Desde o início do desenvolvimento, o projeto foi concebido com foco em:

* Arquitetura em camadas;
* Baixo acoplamento;
* Princípios SOLID;
* Responsabilidade Única (SRP);
* Segurança no gerenciamento de credenciais;
* Facilidade de manutenção e evolução;
* Preparação para suportar múltiplos provedores de e-mail, como Gmail e Outlook.

---

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

Atualmente, a API disponibiliza os seguintes endpoints:

* `GET /emails`
* `GET /emails/{id}`
* `GET /emails/{id}/resumo`

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
│   ├── parser
│   ├── provider
│   └── service
│
├── attachment
│   ├── reader
│   ├── service
│   └── validator
│
├── config
├── controller
├── dto
├── exception
├── provider
└── service
```

A organização dos pacotes segue responsabilidades bem definidas, facilitando a manutenção, os testes e a evolução do sistema.

# 🔒 Segurança

A segurança foi tratada como um requisito desde o início do desenvolvimento do projeto.

Entre as principais medidas adotadas estão:

## Credenciais

* ✅ OAuth 2.0 para autenticação com a Gmail API;
* ✅ Escopo Gmail READONLY;
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

---

# ✅ Qualidade do Código

Durante o desenvolvimento, o projeto passou por um processo contínuo de refatoração para manter uma arquitetura limpa e de fácil manutenção.

Entre as melhorias implementadas estão:

* criação do `GeminiResponseParser` para centralizar a desserialização das respostas da IA;
* separação das responsabilidades entre Provider, Client e Parser;
* tratamento específico para `IOException`, `JsonProcessingException` e `InterruptedException`;
* revisão completa da estratégia de logs;
* padronização da comunicação entre os componentes da aplicação.

Essa organização torna o sistema mais simples de testar, evoluir e manter.

---

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

Defina a chave da API do Gemini como variável de ambiente:

```bash
GEMINI_API_KEY=sua_chave_aqui
```

Adicione o arquivo `credentials.json` (OAuth 2.0) na pasta configurada pela aplicação.

> **Importante:** Nunca versione credenciais, tokens ou chaves de API.

## Executando

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

---

# 📌 Endpoints

| Método | Endpoint              | Descrição                        |
| ------ | --------------------- | -------------------------------- |
| GET    | `/emails`             | Lista os e-mails mais recentes   |
| GET    | `/emails/{id}`        | Retorna os detalhes de um e-mail |
| GET    | `/emails/{id}/resumo` | Gera um resumo utilizando IA     |

---

# 🗺️ Roadmap

## ✅ Fase 1 — Integração com Gmail

* Leitura de e-mails
* OAuth 2.0
* Gmail API
* Extração do corpo da mensagem

**Status:** Concluída

---

## ✅ Fase 2 — Inteligência Artificial

* Integração com Google Gemini
* Geração de resumo estruturado
* Parser dedicado (`GeminiResponseParser`)
* PromptBuilderService
* Tratamento de exceções
* Refatoração da arquitetura
* Testes unitários

**Status:** Concluída

---

## ✅ Fase 2.5 — Processamento de Anexos

* Leitura de arquivos PDF
* Leitura de arquivos DOCX
* Leitura de arquivos TXT
* Validação de anexos
* Inclusão do conteúdo dos anexos na análise da IA

**Status:** Concluída

---

## 🚧 Próximas versões

Entre as evoluções previstas para as próximas versões estão:

* suporte ao Microsoft Outlook;
* novos provedores de Inteligência Artificial;
* processamento de imagens em anexos (OCR);
* painel web para consulta de resumos;
* envio automático dos resumos por e-mail;
* execução agendada;
* suporte a múltiplas contas de e-mail.

---

# 🤝 Contribuição

Contribuições são bem-vindas.

Caso encontre algum problema ou tenha sugestões de melhoria, fique à vontade para abrir uma *Issue* ou enviar um *Pull Request*.

---

# 👩‍💻 Sobre a autora

Desenvolvido por **Samara Silva**, estudante de Análise e Desenvolvimento de Sistemas, com foco em desenvolvimento backend, arquitetura de software, APIs REST e integração com Inteligência Artificial.

Este projeto foi criado como parte da construção do meu portfólio, aplicando boas práticas de engenharia de software, princípios SOLID, segurança, testes automatizados e integração com serviços do Google.
