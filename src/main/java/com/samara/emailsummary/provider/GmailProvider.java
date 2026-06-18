package com.samara.emailsummary.provider;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.google.api.services.gmail.model.MessagePartHeader;

import com.samara.emailsummary.dto.EmailResumoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class GmailProvider implements EmailProvider {

    private final Gmail gmail;

    public GmailProvider(Gmail gmail) {
        this.gmail = gmail;
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

            EmailDetalheDTO email = new EmailDetalheDTO();

            email.setId(id);
            email.setRemetente(remetente);
            email.setAssunto(assunto);
            email.setData(dataFormatada);
            email.setTemAnexo(temAnexo);
            email.setCorpo(corpo);

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
}