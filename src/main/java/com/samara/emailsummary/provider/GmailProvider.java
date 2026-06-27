package com.samara.emailsummary.provider;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.dto.AttachmentMetadataDTO;
import com.samara.emailsummary.attachment.service.AttachmentProcessingService;
import com.samara.emailsummary.attachment.service.AttachmentValidationService;

import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class GmailProvider implements EmailProvider {

    private final Gmail gmail;
    private final AttachmentValidationService attachmentValidationService;
    private final AttachmentProcessingService attachmentProcessingService;

    public GmailProvider(Gmail gmail,
                         AttachmentValidationService attachmentValidationService,
                         AttachmentProcessingService attachmentProcessingService) {
        this.gmail = gmail;
        this.attachmentValidationService = attachmentValidationService;
        this.attachmentProcessingService = attachmentProcessingService;
    }

    @Override
    public EmailResumoDTO obterEmailTeste() {

        EmailResumoDTO email = new EmailResumoDTO();

        email.setRemetente("presidencia@empresa.com");
        email.setAssunto("Reunião da Diretoria");
        email.setResumo("Discussão sobre planejamento institucional.");
        email.setTemAnexo(true);

        return email;
    }

    @Override
    public List<EmailResumoDTO> listarEmails() {
        try {
            List<Message> mensagens = gmail.users()
                    .messages()
                    .list("me")
                    .setMaxResults(5L)
                    .execute()
                    .getMessages();

            if (mensagens == null || mensagens.isEmpty()) {
                return List.of();
            }

            return mensagens.stream()
                    .map(this::buscarDetalhesEmail)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar e-mails do Gmail", e);
        }

    }

    private Message buscarMensagemCompleta(String id) {
        try {
            return gmail.users()
                    .messages()
                    .get("me", id)
                    .setFormat("full")
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar mensagem completa no Gmail", e);
        }
    }

    private EmailResumoDTO buscarDetalhesEmail(Message mensagemResumo) {
        try {
            Message mensagemCompleta = buscarMensagemCompleta(mensagemResumo.getId());

            String assunto = buscarHeader(mensagemCompleta, "Subject");
            String remetente = buscarHeader(mensagemCompleta, "From");
            String snippet = mensagemCompleta.getSnippet();
            String dataOriginal = buscarHeader(mensagemCompleta, "Date");
            String dataFormatada = formatarData(dataOriginal);

            boolean temAnexo = possuiAnexo(mensagemCompleta);

            EmailResumoDTO email = new EmailResumoDTO();

            email.setId(mensagemResumo.getId());
            email.setRemetente(remetente);
            email.setAssunto(assunto);
            email.setResumo(snippet);
            email.setData(dataFormatada);
            email.setTemAnexo(temAnexo);

            return email;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar detalhes do e-mail", e);
        }
    }

    private String buscarHeader(Message mensagem, String nomeHeader) {
        return mensagem.getPayload()
                .getHeaders()
                .stream()
                .filter(header -> header.getName().equalsIgnoreCase(nomeHeader))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("");
    }
    private String formatarData(String dataOriginal) {
        try {
            ZonedDateTime data = ZonedDateTime.parse(
                    dataOriginal,
                    DateTimeFormatter.RFC_1123_DATE_TIME
            );

            return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        } catch (Exception e) {
            return dataOriginal;
        }
    }

    private boolean possuiAnexo(Message mensagem) {
        if (mensagem.getPayload() == null || mensagem.getPayload().getParts() == null) {
            return false;
        }

        return mensagem.getPayload()
                .getParts()
                .stream()
                .anyMatch(part -> part.getFilename() != null && !part.getFilename().isBlank());
    }

    private List<AttachmentMetadataDTO> extrairAnexos(MessagePart payload) {

        List<AttachmentMetadataDTO> anexos = new ArrayList<>();

        if (payload == null || payload.getParts() == null) {
            return anexos;
        }

        for (MessagePart part : payload.getParts()) {

            String nomeArquivo = part.getFilename();

            if (nomeArquivo != null && !nomeArquivo.isBlank()) {

                String attachmentId = part.getBody() != null
                        ? part.getBody().getAttachmentId()
                        : null;

                Long tamanho = part.getBody() != null && part.getBody().getSize() != null
                        ? part.getBody().getSize().longValue()
                        : 0L;

                AttachmentMetadataDTO anexo = new AttachmentMetadataDTO(
                        nomeArquivo,
                        part.getMimeType(),
                        tamanho,
                        attachmentId
                );

                anexos.add(anexo);
            }
        }

        return anexos;
    }

    private String extrairCorpoTexto(Message mensagem) {
        if (mensagem.getPayload() == null) {
            return "";
        }

        return extrairTextoDaParte(mensagem.getPayload());
    }

    private String extrairTextoDaParte(MessagePart parte) {
        if (parte.getMimeType() != null
                && parte.getMimeType().equalsIgnoreCase("text/plain")
                && parte.getBody() != null
                && parte.getBody().getData() != null) {

            byte[] bytes = Base64.getUrlDecoder().decode(parte.getBody().getData());
            return new String(bytes);
        }

        if (parte.getParts() != null) {
            return parte.getParts()
                    .stream()
                    .map(this::extrairTextoDaParte)
                    .filter(texto -> texto != null && !texto.isBlank())
                    .findFirst()
                    .orElse("");
        }

        return "";
    }

    @Override
    public EmailDetalheDTO buscarEmailPorId(String id) {
        try {

            Message mensagemCompleta = buscarMensagemCompleta(id);

            String remetente = buscarHeader(mensagemCompleta, "From");
            String assunto = buscarHeader(mensagemCompleta, "Subject");
            String dataOriginal = buscarHeader(mensagemCompleta, "Date");
            String dataFormatada = formatarData(dataOriginal);
            String corpo = limparCorpoEmail(extrairCorpoTexto(mensagemCompleta));

            boolean temAnexo = possuiAnexo(mensagemCompleta);

            List<AttachmentMetadataDTO> anexos = extrairAnexos(mensagemCompleta.getPayload());

            String textoDosAnexos = extrairTextoDosAnexos(id, anexos);

            EmailDetalheDTO email = new EmailDetalheDTO();

            email.setId(id);
            email.setThreadId(mensagemCompleta.getThreadId());
            email.setRemetente(remetente);
            email.setAssunto(assunto);
            email.setData(dataFormatada);
            email.setTemAnexo(temAnexo);
            email.setCorpo(juntarConteudo(corpo, textoDosAnexos));
            email.setAnexos(anexos);

            return email;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar e-mail por ID", e);
        }

    }

    private String limparCorpoEmail(String corpo) {

        if (corpo == null || corpo.isBlank()) {
            return "";
        }

        String corpoLimpo = corpo
                .replaceAll("\\r\\n", "\n")
                .replaceAll("\\n{3,}", "\n\n")
                .replaceAll("[ \\t]{2,}", " ")
                .trim();

        String[] marcadoresHistorico = {
                "\nEm ",
                "\nDe:",
                "\nFrom:",
                "\nEnviada em:",
                "---------- Forwarded message ---------"
        };

        int menorIndice = -1;

        for (String marcador : marcadoresHistorico) {
            int indice = corpoLimpo.indexOf(marcador);

            if (indice > 0 && (menorIndice == -1 || indice < menorIndice)) {
                menorIndice = indice;
            }
        }

        if (menorIndice > 0) {
            corpoLimpo = corpoLimpo.substring(0, menorIndice).trim();
        }

        return corpoLimpo;
    }

    private byte[] baixarAnexo(String mensagemId, String attachmentId) {
        try {
            MessagePartBody anexo = gmail.users()
                    .messages()
                    .attachments()
                    .get("me", mensagemId, attachmentId)
                    .execute();

            return Base64.getUrlDecoder().decode(anexo.getData());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar anexo do Gmail", e);
        }
    }

    private String extrairTextoDosAnexos(String mensagemId, List<AttachmentMetadataDTO> anexos) {

        if (anexos == null || anexos.isEmpty()) {
            return "";
        }

        StringBuilder textoDosAnexos = new StringBuilder();

        for (AttachmentMetadataDTO anexo : anexos) {
            textoDosAnexos.append(processarAnexo(mensagemId, anexo));
        }

        return textoDosAnexos.toString().trim();
    }

    private String processarAnexo(String mensagemId, AttachmentMetadataDTO anexo) {

        try {
            attachmentValidationService.validar(anexo);

            byte[] arquivo = baixarAnexo(mensagemId, anexo.getAttachmentId());

            String textoExtraido = attachmentProcessingService.extrairTexto(
                    anexo.getMimeType(),
                    arquivo
            );

            if (textoExtraido == null || textoExtraido.isBlank()) {
                return "";
            }

            return "\n\n--- Anexo: "
                    + anexo.getNomeArquivo()
                    + " ---\n"
                    + textoExtraido;

        } catch (Exception e) {

            return "\n\n--- Anexo ignorado: "
                    + anexo.getNomeArquivo()
                    + " ---\nMotivo: "
                    + e.getMessage();
        }
    }

    private String juntarConteudo(String corpo, String textoDosAnexos) {

        if (textoDosAnexos == null || textoDosAnexos.isBlank()) {
            return corpo;
        }

        return corpo +
                "\n\n==================== ANEXOS ====================\n\n" +
                textoDosAnexos;
    }

    @Override
    public List<EmailDetalheDTO> buscarEmailsPorThreadId(String threadId) {
        try {
            Thread thread = gmail.users()
                    .threads()
                    .get("me", threadId)
                    .setFormat("full")
                    .execute();

            if (thread.getMessages() == null || thread.getMessages().isEmpty()) {
                return List.of();
            }

            return thread.getMessages()
                    .stream()
                    .map(message -> buscarEmailPorId(message.getId()))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar e-mails por thread ID", e);
        }
    }

}