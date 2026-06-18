# Email Summary

🟢 Java 21
🟢 Spring Boot
🟢 Gmail API
🟢 OAuth 2.0

## Sobre o projeto

O Email Summary é uma aplicação desenvolvida em Java com Spring Boot para leitura de e-mails utilizando a API do Gmail.

O objetivo do projeto é automatizar a obtenção de mensagens, preparar seu conteúdo para processamento por Inteligência Artificial e, futuramente, gerar resumos executivos, processar anexos, emitir relatórios e integrar diferentes provedores de e-mail.

O projeto foi desenvolvido priorizando arquitetura em camadas, baixo acoplamento, reutilização de código e segurança no tratamento das credenciais de autenticação.

## Funcionalidades implementadas

Atualmente o projeto é capaz de:

- Autenticar de forma segura utilizando OAuth 2.0 do Google.
- Ler e-mails da conta Gmail utilizando a Gmail API.
- Identificar a presença de anexos nas mensagens.
- Listar os e-mails mais recentes.
- Consultar os detalhes completos de um e-mail pelo ID.
- Extrair remetente, assunto, data, corpo da mensagem e indicação de anexos.
- Realizar uma limpeza inicial do corpo do e-mail, removendo trechos de histórico e encaminhamentos para preparar o texto para processamento por Inteligência Artificial (IA).
- Utilizar uma arquitetura baseada em camadas, facilitando manutenção e evolução do sistema.

## Arquitetura

O projeto foi desenvolvido seguindo uma arquitetura em camadas, onde cada componente possui uma responsabilidade bem definida.

```
Controller
        ↓
Service
        ↓
EmailProvider
        ↓
GmailProvider
        ↓
GmailConfig
        ↓
GoogleAuthorizationService
        ↓
Gmail API
```

### Responsabilidades

- **Controller:** recebe as requisições HTTP.
- **Service:** coordena o fluxo da aplicação e aplica as regras de negócio.
- **EmailProvider:** define o contrato para provedores de e-mail.
- **GmailProvider:** implementa a integração com a Gmail API.
- **DTOs:** transportam os dados entre as camadas.
- **GoogleAuthorizationService:** realiza a autenticação OAuth 2.0.
- **GmailConfig:** configura e disponibiliza o cliente da Gmail API através de injeção de dependência.

## Tecnologias

- Java 21
- Spring Boot 3.5.15
- Maven
- REST API
- Gmail API
- Google OAuth 2.0
- IntelliJ IDEA
- Git
- GitHub

## Estrutura do projeto

```
src/main/java/com/samara/emailsummary

├── config
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

## Segurança

A segurança foi considerada desde o início do desenvolvimento do projeto.

As principais práticas adotadas incluem:

- Utilização do OAuth 2.0 para autenticação com a conta Google.
- Utilização de injeção de dependência para desacoplar a configuração da Gmail API.
- Uso do escopo `GMAIL_READONLY`, seguindo o princípio do menor privilégio (Least Privilege).
- Separação entre autenticação, configuração e regra de negócio.
- Credenciais OAuth (`credentials.json`) não versionadas no Git.
- Tokens de autenticação armazenados fora do repositório.
- Arquivos sensíveis protegidos através do `.gitignore`.
- Arquitetura preparada para facilitar manutenção, auditoria e evolução do sistema.

## Roadmap

Próximas funcionalidades previstas:

- [ ] Processamento seguro de anexos.
- [ ] Integração com o Google Gemini para geração de resumos.
- [ ] Geração de relatórios executivos.
- [ ] Envio automático de relatórios.
- [ ] Suporte ao Microsoft Outlook.
- [ ] Testes unitários.
- [ ] Testes de integração.

## Como executar

### Pré-requisitos

- Java 21
- Maven 3.9+
- Conta Google com a Gmail API habilitada
- Credenciais OAuth 2.0

### Passos

Clone este repositório.

Configure o arquivo `credentials.json` no diretório:

```text
src/main/resources/google/
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

ou

```bash
mvn spring-boot:run
```

Na primeira execução será aberta uma janela do navegador para autorização da conta Google.

Após a autorização, os tokens serão armazenados localmente em:

```text
data/
```

A aplicação estará disponível em:

```text
http://localhost:8080/emails
```

Para consultar um e-mail específico:

```text
http://localhost:8080/emails/{id}
```

> **Importante:** O arquivo `credentials.json` e a pasta `data/` não fazem parte do repositório por questões de segurança.
## Autora

Desenvolvido por **Samara Silva**.

Este projeto faz parte do meu portfólio de desenvolvimento em Java e Spring Boot, com foco em arquitetura de software, integração com APIs, segurança e boas práticas de desenvolvimento.ojeto desenvolvido como parte dos estudos em Java e Spring Boot, com foco em integração com APIs, arquitetura de software, segurança e boas práticas de desenvolvimento.
