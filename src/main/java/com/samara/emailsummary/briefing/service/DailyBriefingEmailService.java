package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.ai.dto.AnalysisType;
import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.service.SummaryService;
import com.samara.emailsummary.briefing.context.DailyBriefingContextBuilder;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import com.samara.emailsummary.briefing.dto.EmailCategory;
import com.samara.emailsummary.briefing.dto.EmailClassificationResult;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.service.EmailSenderService;
import com.samara.emailsummary.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyBriefingEmailService {

    private static final Logger log = LoggerFactory.getLogger(DailyBriefingEmailService.class);

    private final EmailService emailService;
    private final SummaryService summaryService;
    private final DailyBriefingService dailyBriefingService;
    private final DailyBriefingFormatterService dailyBriefingFormatterService;
    private final EmailClassificationService emailClassificationService;
    private final EmailSenderService emailSenderService;
    private final DailyBriefingContextBuilder dailyBriefingContextBuilder;

    @Value("${email.summary.destination}")
    private String destination;

    public DailyBriefingEmailService(
            EmailService emailService,
            SummaryService summaryService,
            DailyBriefingService dailyBriefingService,
            DailyBriefingFormatterService dailyBriefingFormatterService,
            EmailClassificationService emailClassificationService,
            EmailSenderService emailSenderService,
            DailyBriefingContextBuilder dailyBriefingContextBuilder
    ) {
        this.emailService = emailService;
        this.summaryService = summaryService;
        this.dailyBriefingService = dailyBriefingService;
        this.dailyBriefingFormatterService = dailyBriefingFormatterService;
        this.emailClassificationService = emailClassificationService;
        this.emailSenderService = emailSenderService;
        this.dailyBriefingContextBuilder = dailyBriefingContextBuilder;
    }

    public void enviarBriefingDiario() {

        List<EmailResumoDTO> emails = emailService.listarEmails();

        Map<String, EmailDetalheDTO> emailsPorConversa = new LinkedHashMap<>();

        for (EmailResumoDTO emailResumo : emails) {
            try {
                EmailDetalheDTO email = emailService.buscarEmailPorId(emailResumo.getId());

                if (deveIgnorarEmail(email.getAssunto())) {
                    continue;
                }

                String chaveConversa = obterChaveConversa(email);

                emailsPorConversa.putIfAbsent(chaveConversa, email);

            } catch (Exception e) {
                log.warn("Falha ao buscar um e-mail no Daily Briefing. O processamento continuará.", e);
            }
        }

        List<DailyBriefingItem> itens = new ArrayList<>();

        int numero = 1;

        for (EmailDetalheDTO email : emailsPorConversa.values()) {

            EmailClassificationResult classificacao = emailClassificationService.classificar(
                    email.getAssunto(),
                    email.getRemetente(),
                    email.getCorpo()
            );

            SummaryResponse resumo = gerarResumoSeguro(email, classificacao);

            DailyBriefingItem item = new DailyBriefingItem(
                    numero,
                    email,
                    resumo
            );

            itens.add(item);
            numero++;
        }

        DailyBriefing briefing = dailyBriefingService.criarBriefing(itens);

        SummaryResponse briefingInteligente = gerarBriefingInteligenteSeguro();

        String corpo = dailyBriefingFormatterService.formatar(
                briefing,
                briefingInteligente.resumo()
        );

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        emailSenderService.enviarResumo(
                destination,
                "Daily Briefing - " + data,
                corpo
        );
    }

    public SummaryResponse gerarBriefingInteligente() {

        List<EmailResumoDTO> emails = emailService.listarEmails();

        List<EmailDetalheDTO> emailsDetalhados = new ArrayList<>();

        for (EmailResumoDTO emailResumo : emails) {
            EmailDetalheDTO email = emailService.buscarEmailPorId(emailResumo.getId());

            if (deveIgnorarEmail(email.getAssunto())) {
                continue;
            }

            emailsDetalhados.add(email);
        }

        List<EmailDetalheDTO> emailsOrdenadosPorRelevancia = emailsDetalhados.stream()
                .sorted(Comparator.comparingInt(this::obterPesoRelevancia).reversed())
                .toList();

        String contexto = dailyBriefingContextBuilder.construirContexto(emailsOrdenadosPorRelevancia);

        SummaryRequest request = new SummaryRequest(
                AnalysisType.DAILY_BRIEFING,
                "Briefing diário de e-mails",
                "Sistema Email Summary",
                contexto
        );

        return summaryService.gerarResumo(request);
    }

    public void enviarBriefingInteligente() {
        SummaryResponse briefing = gerarBriefingInteligenteSeguro();

        String divisor = "═══════════════════════════════\n";

        String dataHora = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy • HH:mm"));

        StringBuilder corpo = new StringBuilder();

        corpo.append(divisor);
        corpo.append("📨 DAILY BRIEFING INTELIGENTE\n\n");
        corpo.append(dataHora).append("\n");
        corpo.append(divisor).append("\n");

        corpo.append("📌 RESUMO EXECUTIVO\n\n");
        corpo.append(briefing.resumo()).append("\n\n");

        corpo.append(divisor).append("\n");

        corpo.append("🔥 PRIORIDADE\n\n");
        corpo.append(briefing.prioridade()).append("\n\n");

        corpo.append(divisor).append("\n");

        corpo.append("✅ AÇÕES SUGERIDAS\n\n");
        if (briefing.acoesSugeridas() == null || briefing.acoesSugeridas().isEmpty()) {
            corpo.append("• Nenhuma ação sugerida.\n");
        } else {
            briefing.acoesSugeridas().forEach(acao ->
                    corpo.append("• ").append(acao).append("\n")
            );
        }
        corpo.append("\n");

        corpo.append(divisor).append("\n");

        corpo.append("📋 PENDÊNCIAS\n\n");
        if (briefing.pendencias() == null || briefing.pendencias().isEmpty()) {
            corpo.append("• Nenhuma pendência identificada.\n");
        } else {
            briefing.pendencias().forEach(pendencia ->
                    corpo.append("• ").append(pendencia).append("\n")
            );
        }
        corpo.append("\n");

        corpo.append(divisor).append("\n");

        corpo.append("📅 PRAZOS\n\n");
        if (briefing.prazos() == null || briefing.prazos().isEmpty()) {
            corpo.append("• Nenhum prazo identificado.\n");
        } else {
            briefing.prazos().forEach(prazo ->
                    corpo.append("• ").append(prazo).append("\n")
            );
        }
        corpo.append("\n");

        corpo.append(divisor).append("\n");

        corpo.append("👥 PESSOAS CITADAS\n\n");
        if (briefing.pessoasCitadas() == null || briefing.pessoasCitadas().isEmpty()) {
            corpo.append("• Nenhuma pessoa citada.\n");
        } else {
            briefing.pessoasCitadas().forEach(pessoa ->
                    corpo.append("• ").append(pessoa).append("\n")
            );
        }
        corpo.append("\n");

        corpo.append(divisor).append("\n");

        corpo.append("✉️ NECESSITA RESPOSTA\n\n");
        corpo.append(briefing.necessitaResposta() ? "Sim" : "Não").append("\n\n");

        if (briefing.sugestaoResposta() != null && !briefing.sugestaoResposta().isBlank()) {
            corpo.append(divisor).append("\n");
            corpo.append("💬 SUGESTÃO DE RESPOSTA\n\n");
            corpo.append(briefing.sugestaoResposta()).append("\n\n");
        }

        corpo.append(divisor).append("\n");

        corpo.append("🎯 NÍVEL DE CONFIANÇA\n\n");
        corpo.append(briefing.nivelConfianca()).append("\n\n");

        corpo.append(divisor);
        corpo.append("Fim do briefing.");

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        emailSenderService.enviarResumo(
                destination,
                "Daily Briefing Inteligente - " + data,
                corpo.toString()
        );
    }

    private SummaryResponse gerarResumoSeguro(
            EmailDetalheDTO email,
            EmailClassificationResult classificacao
    ) {
        try {
            if (classificacao.categoria() == EmailCategory.NEWSLETTER) {
                String prioridadeFallback = converterPrioridadeLocal(classificacao.categoria());

                return new SummaryResponse(
                        criarResumoSimplificado(email),
                        prioridadeFallback,
                        List.of(),
                        List.of("Verificar este e-mail manualmente."),
                        List.of(),
                        List.of(),
                        false,
                        "",
                        "Baixa"
                );
            }

            SummaryRequest request = new SummaryRequest(
                    AnalysisType.EMAIL_SUMMARY,
                    email.getAssunto(),
                    email.getRemetente(),
                    email.getCorpo()
            );

            return summaryService.gerarResumo(request);

        } catch (Exception e) {
            log.warn("Falha ao gerar resumo com IA. O e-mail será incluído com resumo simplificado.", e);

            return new SummaryResponse(
                    criarResumoSimplificado(email),
                    "Média",
                    List.of(),
                    List.of("Verificar este e-mail manualmente."),
                    List.of(),
                    List.of(),
                    false,
                    "",
                    "Baixa"
            );
        }
    }

    private SummaryResponse gerarBriefingInteligenteSeguro() {
        try {
            return gerarBriefingInteligente();

        } catch (Exception e) {
            log.warn("Falha ao gerar o resumo geral do Daily Briefing. Será usado texto alternativo.", e);

            return new SummaryResponse(
                    "Não foi possível gerar o resumo geral com IA. Consulte os e-mails listados por prioridade abaixo.",
                    "Média",
                    List.of(),
                    List.of(),
                    List.of(),
                    List.of(),
                    false,
                    "",
                    "Baixa"
            );
        }
    }

    private int obterPesoRelevancia(EmailDetalheDTO email) {
        EmailClassificationResult classificacao = emailClassificationService.classificar(
                email.getAssunto(),
                email.getRemetente(),
                email.getCorpo()
        );

        return classificacao.categoria().getPeso();
    }

    private String obterChaveConversa(EmailDetalheDTO email) {
        if (email.getThreadId() != null && !email.getThreadId().isBlank()) {
            return email.getThreadId();
        }

        if (email.getAssunto() != null && !email.getAssunto().isBlank()) {
            return email.getAssunto();
        }

        return email.getId();
    }

    private boolean deveIgnorarEmail(String assunto) {
        if (assunto == null || assunto.isBlank()) {
            return false;
        }

        String assuntoNormalizado = assunto.trim().toLowerCase();

        return assuntoNormalizado.startsWith("resumo do e-mail:")
                || assuntoNormalizado.startsWith("daily briefing")
                || assuntoNormalizado.startsWith("briefing da manhã");
    }

    private String converterPrioridadeLocal(EmailCategory categoria) {
        return switch (categoria) {
            case CRITICAL, HIGH_PRIORITY -> "Alta";
            case ACTION_REQUIRED -> "Média";
            case INFORMATIONAL, NEWSLETTER -> "Baixa";
        };
    }

    private String criarResumoSimplificado(EmailDetalheDTO email) {
        if (email.getCorpo() == null || email.getCorpo().isBlank()) {
            return "Não foi possível gerar o resumo com IA. Verifique o e-mail manualmente.";
        }

        String corpo = email.getCorpo().trim();

        if (corpo.length() <= 400) {
            return corpo;
        }

        return corpo.substring(0, 400) + "...";
    }
}