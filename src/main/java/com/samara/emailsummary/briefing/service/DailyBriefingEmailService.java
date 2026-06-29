package com.samara.emailsummary.briefing.service;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.service.SummaryService;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import com.samara.emailsummary.briefing.dto.EmailCategory;
import com.samara.emailsummary.briefing.dto.EmailClassificationResult;
import com.samara.emailsummary.briefing.context.DailyBriefingContextBuilder;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.service.EmailSenderService;
import com.samara.emailsummary.service.EmailService;
import com.samara.emailsummary.ai.dto.AnalysisType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

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

        List<DailyBriefingItem> itens = new ArrayList<>();

        int numero = 1;

        for (EmailResumoDTO emailResumo : emails) {

            try {
                EmailDetalheDTO email = emailService.buscarEmailPorId(emailResumo.getId());

                if (email.getAssunto() != null &&
                        email.getAssunto().startsWith("Resumo do e-mail:")) {
                    continue;
                }

                EmailClassificationResult classificacao =
                        emailClassificationService.classificar(
                                email.getAssunto(),
                                email.getRemetente(),
                                email.getCorpo()
                        );

                SummaryResponse resumo;

                if (classificacao.categoria() == EmailCategory.NEWSLETTER) {
                    resumo = new SummaryResponse(
                            "E-mail identificado como informativo/newsletter.",
                            "Baixa",
                            List.of(),
                            List.of(),
                            List.of(),
                            List.of(),
                            false,
                            "",
                            "Alta"
                    );
                } else {
                    SummaryRequest request = new SummaryRequest(
                            AnalysisType.EMAIL_SUMMARY,
                            email.getAssunto(),
                            email.getRemetente(),
                            email.getCorpo()
                    );

                    resumo = summaryService.gerarResumo(request);
                }

                DailyBriefingItem item = new DailyBriefingItem(
                        numero,
                        email,
                        resumo
                );

                itens.add(item);
                numero++;

            } catch (Exception e) {
                log.warn("Falha ao processar um e-mail no Daily Briefing. O processamento continuará.", e);
            }
        }

        DailyBriefing briefing = dailyBriefingService.criarBriefing(itens);

        String corpo = dailyBriefingFormatterService.formatar(briefing);

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

            if (email.getAssunto() != null &&
                    email.getAssunto().startsWith("Resumo do e-mail:")) {
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
        SummaryResponse briefing = gerarBriefingInteligente();

        StringBuilder corpo = new StringBuilder();

        corpo.append("DAILY BRIEFING INTELIGENTE\n");
        corpo.append("==========================\n\n");

        corpo.append("Resumo Executivo:\n");
        corpo.append(briefing.resumo()).append("\n\n");

        corpo.append("Prioridade: ");
        corpo.append(briefing.prioridade()).append("\n\n");

        corpo.append("Ações sugeridas:\n");
        briefing.acoesSugeridas().forEach(acao ->
                corpo.append("- ").append(acao).append("\n")
        );
        corpo.append("\n");

        corpo.append("Pendências:\n");
        briefing.pendencias().forEach(pendencia ->
                corpo.append("- ").append(pendencia).append("\n")
        );
        corpo.append("\n");

        corpo.append("Prazos:\n");
        briefing.prazos().forEach(prazo ->
                corpo.append("- ").append(prazo).append("\n")
        );
        corpo.append("\n");

        corpo.append("Pessoas citadas:\n");
        briefing.pessoasCitadas().forEach(pessoa ->
                corpo.append("- ").append(pessoa).append("\n")
        );
        corpo.append("\n");

        corpo.append("Necessita resposta: ");
        corpo.append(briefing.necessitaResposta() ? "Sim" : "Não").append("\n\n");

        if (briefing.sugestaoResposta() != null && !briefing.sugestaoResposta().isBlank()) {
            corpo.append("Sugestão de resposta:\n");
            corpo.append(briefing.sugestaoResposta()).append("\n\n");
        }

        corpo.append("Nível de confiança: ");
        corpo.append(briefing.nivelConfianca()).append("\n");

        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        emailSenderService.enviarResumo(
                destination,
                "Daily Briefing Inteligente - " + data,
                corpo.toString()
        );
    }

    private int obterPesoRelevancia(EmailDetalheDTO email) {
        EmailClassificationResult classificacao = emailClassificationService.classificar(
                email.getAssunto(),
                email.getRemetente(),
                email.getCorpo()
        );

        return classificacao.categoria().getPeso();
    }

}