# Email Summary

🟢 Java 21
🟢 Spring Boot
🟢 Gmail API
🟢 OAuth 2.0

## Sobre o projeto

O Email Summary é uma aplicação backend desenvolvida para automatizar a leitura de e-mails corporativos, extrair informações relevantes e gerar resumos executivos utilizando Inteligência Artificial.

A aplicação integra-se à Gmail API através de OAuth 2.0, processa o conteúdo das mensagens e de seus anexos e utiliza o Google Gemini para produzir análises estruturadas, incluindo resumo, prioridade, pendências, ações sugeridas e necessidade de resposta.

Desde o início do desenvolvimento, o projeto foi construído priorizando:

Arquitetura em camadas
Baixo acoplamento
Princípios SOLID
Responsabilidade Única (SRP)
Segurança das credenciais
Facilidade de manutenção e evolução
Preparação para múltiplos provedores de e-mail (Gmail e Outlook)

## ✨ Funcionalidades

Atualmente a aplicação é capaz de:

### 📧 Leitura de e-mails

* ✅ Autenticação segura utilizando OAuth 2.0 do Google
* ✅ Integração com a Gmail API
* ✅ Listagem dos e-mails mais recentes
* ✅ Consulta detalhada de um e-mail pelo ID
* ✅ Extração de:

  * remetente
  * assunto
  * data
  * corpo da mensagem
  * indicação de anexos

### 📎 Processamento de anexos

* ✅ Leitura de arquivos PDF
* ✅ Leitura de documentos DOCX
* ✅ Leitura de arquivos TXT
* ✅ Validação de tipo, tamanho e nome do arquivo
* ✅ Incorporação automática do conteúdo dos anexos ao texto do e-mail

### 🧹 Preparação do conteúdo

* ✅ Remoção de assinaturas
* ✅ Remoção de históricos de conversas
* ✅ Remoção de encaminhamentos
* ✅ Normalização do texto para processamento por IA

### 🤖 Inteligência Artificial

* ✅ Integração com Google Gemini
* ✅ Construção dinâmica de prompts
* ✅ Geração automática de resumo executivo
* ✅ Classificação automática de prioridade
* ✅ Identificação de:

  * ações sugeridas
  * pendências
  * prazos
  * pessoas citadas
  * necessidade de resposta
* ✅ Geração de sugestão de resposta quando necessário
* ✅ Conversão automática da resposta da IA para objetos Java utilizando Jackson (`ObjectMapper`)

### 🌐 API REST

Endpoints disponíveis:

* `GET /emails`
* `GET /emails/{id}`
* `GET /emails/{id}/resumo`


## 🏗️ Arquitetura

O projeto foi desenvolvido seguindo uma arquitetura em camadas, com responsabilidades bem definidas e baixo acoplamento entre os componentes.

### Fluxo da aplicação

```text
Cliente HTTP
      │
      ▼
EmailController
      │
      ▼
EmailService
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

### Fluxo da Inteligência Artificial

```text
EmailController
        │
        ▼
SummaryService
        │
        ├──────────────┐
        ▼              ▼
PromptBuilder     AiProvider
Service                │
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
               SummaryResponse
```

### Processamento de anexos

```text
AttachmentValidationService
              │
              ▼
AttachmentProcessingService
              │
              ▼
AttachmentReader
      ├─────────────┬─────────────┐
      ▼             ▼             ▼
PdfReader      DocxReader     TextReader
```

## Responsabilidades

| Componente                    | Responsabilidade                                   |
| ----------------------------- | -------------------------------------------------- |
| `Controller`                  | Recebe as requisições HTTP                         |
| `Service`                     | Coordena o fluxo da aplicação                      |
| `Provider`                    | Define contratos para integrações externas         |
| `GmailProvider`               | Implementa a integração com a Gmail API            |
| `GeminiProvider`              | Implementa a integração com o Google Gemini        |
| `GeminiClient`                | Realiza a comunicação HTTP com a API do Gemini     |
| `PromptBuilderService`        | Constrói prompts para a IA                         |
| `AttachmentProcessingService` | Processa os anexos                                 |
| `AttachmentReader`            | Define o contrato para leitura de anexos           |
| `DTOs`                        | Transportam dados entre as camadas                 |
| `Config`                      | Centraliza configurações e injeção de dependências |

### Princípios adotados

* ✅ SOLID
* ✅ Single Responsibility Principle (SRP)
* ✅ Baixo acoplamento
* ✅ Separação de responsabilidades
* ✅ Injeção de dependência
* ✅ Arquitetura extensível para novos provedores


## 🛠️ Tecnologias

### Linguagem e Framework

* Java 21
* Spring Boot 3.5.x
* Maven

### APIs e Integrações

* Gmail API
* Google OAuth 2.0
* Google Gemini API

### Processamento de Arquivos

* Apache PDFBox (PDF)
* Apache POI (DOCX)
* Java Nativo (TXT)

### Serialização

* Jackson (`ObjectMapper`)

### Comunicação HTTP

* Java HttpClient

### Desenvolvimento

* IntelliJ IDEA
* Git
* GitHub

### Arquitetura

* REST API
* DTO Pattern
* Provider Pattern
* Dependency Injection
* SOLID
* Single Responsibility Principle (SRP)


## 📁 Estrutura do Projeto

```text
src
└── main
    ├── java
    │   └── com
    │       └── samara
    │           └── emailsummary
    │
    ├── ai
    │   ├── client
    │   │   └── GeminiClient
    │   │
    │   ├── dto
    │   │   ├── Candidate
    │   │   ├── Content
    │   │   ├── GeminiRequest
    │   │   ├── GeminiResponse
    │   │   ├── Part
    │   │   ├── SummaryRequest
    │   │   └── SummaryResponse
    │   │
    │   ├── provider
    │   │   ├── AiProvider
    │   │   └── GeminiProvider
    │   │
    │   └── service
    │       ├── PromptBuilderService
    │       └── SummaryService
    │
    ├── attachment
    │   ├── reader
    │   │   ├── AttachmentReader
    │   │   ├── PdfAttachmentReader
    │   │   ├── DocxAttachmentReader
    │   │   └── TextAttachmentReader
    │   │
    │   └── service
    │       ├── AttachmentProcessingService
    │       └── AttachmentValidationService
    │
    ├── config
    │   ├── AiProperties
    │   ├── GeminiConfig
    │   └── GmailConfig
    │
    ├── controller
    │   ├── EmailController
    │   └── HelloController
    │
    ├── dto
    │   ├── EmailDetalheDTO
    │   └── EmailResumoDTO
    │
    ├── provider
    │   ├── EmailProvider
    │   └── GmailProvider
    │
    ├── security
    │   └── oauth
    │       └── GoogleAuthorizationService
    │
    ├── service
    │   ├── EmailService
    │   └── HelloService
    │
    └── EmailSummaryApplication
```


## 🔐 Segurança

A segurança foi considerada desde o início do desenvolvimento do projeto.

### Autenticação

* ✅ OAuth 2.0 com a Google Gmail API
* ✅ Escopo `GMAIL_READONLY`, seguindo o princípio do menor privilégio (Least Privilege)
* ✅ Tokens armazenados fora do repositório

### Proteção de credenciais

* ✅ `credentials.json` não versionado
* ✅ API Key do Google Gemini armazenada em variável de ambiente
* ✅ Arquivos sensíveis protegidos pelo `.gitignore`
* ✅ Nenhum segredo armazenado no código-fonte

### Tratamento seguro de erros

* ✅ Exceções internas registradas em log
* ✅ Mensagens genéricas retornadas ao cliente
* ✅ Nenhuma exposição de:

  * API Keys
  * OAuth Tokens
  * URLs sensíveis
  * Credenciais

### Arquitetura

* ✅ Separação entre configuração, autenticação e regra de negócio
* ✅ Baixo acoplamento entre os módulos
* ✅ Configuração centralizada através de injeção de dependência
* ✅ Estrutura preparada para suportar novos provedores de e-mail e IA

### Boas práticas adotadas

* Princípios SOLID
* Single Responsibility Principle (SRP)
* DTO Pattern
* Provider Pattern
* Dependency Injection
* Variáveis de ambiente para informações sensíveis


## 🚀 Como executar o projeto

### Pré-requisitos

* Java 21
* Maven 3.9+
* Conta Google com acesso à Gmail API
* Projeto criado no Google Cloud Console
* Credenciais OAuth 2.0
* Chave de API do Google Gemini

### 1. Clone o repositório

```bash
git clone https://github.com/samarasp/email-summary.git
```

```bash
cd email-summary
```

### 2. Configure as credenciais OAuth

Coloque o arquivo `credentials.json` no local esperado pela aplicação.

> **Importante:** esse arquivo não é versionado e deve permanecer fora do Git.

### 3. Configure a variável de ambiente

Windows (PowerShell):

```powershell
setx GEMINI_API_KEY "SUA_API_KEY"
```

Após executar o comando, feche e abra novamente o terminal.

### 4. Configure o `application.properties`

A aplicação utiliza:

```properties
email.summary.ai.enabled=true
email.summary.ai.provider=gemini

gemini.api.key=${GEMINI_API_KEY}
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
```

### 5. Execute a aplicação

```bash
mvn spring-boot:run
```

Ou execute a classe:

```text
EmailSummaryApplication
```

### 6. Endpoints disponíveis

| Método | Endpoint              | Descrição                                         |
| ------ | --------------------- | ------------------------------------------------- |
| GET    | `/emails`             | Lista os e-mails mais recentes                    |
| GET    | `/emails/{id}`        | Retorna os detalhes completos de um e-mail        |
| GET    | `/emails/{id}/resumo` | Gera um resumo utilizando Inteligência Artificial |

## 🗺️ Roadmap

### ✅ Concluído

* [x] Estrutura inicial do projeto com Spring Boot
* [x] Arquitetura em camadas
* [x] Integração com Gmail API
* [x] Autenticação OAuth 2.0
* [x] Leitura de e-mails
* [x] Consulta de e-mails por ID
* [x] Extração de remetente, assunto, data e corpo
* [x] Processamento de anexos (PDF, DOCX e TXT)
* [x] Limpeza inteligente do corpo dos e-mails
* [x] Arquitetura para provedores de e-mail
* [x] Integração com Google Gemini
* [x] Construção dinâmica de prompts
* [x] Geração automática de resumo executivo
* [x] Classificação automática de prioridade
* [x] Identificação de pendências, prazos e ações sugeridas
* [x] Desserialização automática da resposta da IA
* [x] Tratamento seguro de credenciais
* [x] API REST para consulta dos resumos

---

### 🚧 Em desenvolvimento

* [ ] Refatoração da integração com IA
* [ ] Parser dedicado para respostas do Gemini
* [ ] Melhoria do tratamento de exceções
* [ ] Ampliação da cobertura de testes unitários

---

### 📌 Próximas funcionalidades

* [ ] Suporte ao Microsoft Outlook
* [ ] Dashboard para consulta dos resumos
* [ ] Histórico de resumos gerados
* [ ] Filtros por prioridade
* [ ] Envio automático de resumos por e-mail
* [ ] Versão Desktop
* [ ] Execução agendada (Scheduler)
* [ ] Geração de relatórios executivos
* [ ] Suporte a novos provedores de IA
* [ ] Testes de integração


Desenvolvido por **Samara Silva**.

Este projeto faz parte do meu portfólio de desenvolvimento em Java e Spring Boot, com foco em arquitetura de software, integração com APIs, segurança e boas práticas de desenvolvimento.ojeto desenvolvido como parte dos estudos em Java e Spring Boot, com foco em integração com APIs, arquitetura de software, segurança e boas práticas de desenvolvimento.
