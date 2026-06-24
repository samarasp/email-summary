package com.samara.emailsummary.controller;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.service.SummaryService;
import com.samara.emailsummary.ai.exception.AiCommunicationException;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;

import com.samara.emailsummary.service.EmailService;
import com.samara.emailsummary.service.EmailSummaryService;

import com.samara.emailsummary.briefing.context.EmailContext;
import com.samara.emailsummary.briefing.context.EmailContextBuilder;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import com.samara.emailsummary.briefing.service.DailyBriefingFormatterService;
import com.samara.emailsummary.briefing.service.DailyBriefingService;
import com.samara.emailsummary.briefing.dto.EmailCategory;
import com.samara.emailsummary.briefing.service.EmailClassificationService;
import com.samara.emailsummary.briefing.dto.EmailClassificationResult;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/emails")
public class EmailController {
    private final EmailService emailService;
    private final SummaryService summaryService;
    private final DailyBriefingService dailyBriefingService;
    private final DailyBriefingFormatterService dailyBriefingFormatterService;
    private final EmailClassificationService emailClassificationService;
    private final EmailSummaryService emailSummaryService;
    private final EmailContextBuilder emailContextBuilder;

    public EmailController(
            EmailService emailService,
            SummaryService summaryService,
            DailyBriefingService dailyBriefingService,
            DailyBriefingFormatterService dailyBriefingFormatterService,
            EmailClassificationService emailClassificationService,
            EmailSummaryService emailSummaryService,
            EmailContextBuilder emailContextBuilder

    ) {
        this.emailService = emailService;
        this.summaryService = summaryService;
        this.dailyBriefingService = dailyBriefingService;
        this.dailyBriefingFormatterService = dailyBriefingFormatterService;
        this.emailClassificationService = emailClassificationService;
        this.emailSummaryService = emailSummaryService;
        this.emailContextBuilder = emailContextBuilder;
     }

    @GetMapping("/teste")
    public EmailResumoDTO obterEmailTeste() {
        return emailService.obterEmailTeste();
    }

    @GetMapping
    public List<EmailResumoDTO> listarEmails() {
        return emailService.listarEmails();
    }

    @GetMapping("/{id}")
    public EmailDetalheDTO buscarEmailPorId(@PathVariable String id) {
        return emailService.buscarEmailPorId(id);
    }

    @GetMapping("/{id}/resumo")
    public SummaryResponse gerarResumo(@PathVariable String id) {
        EmailDetalheDTO email = emailService.buscarEmailPorId(id);

        EmailContext contexto = emailContextBuilder.criarContexto(email);

        SummaryRequest request = new SummaryRequest(
                email.getAssunto(),
                email.getRemetente(),
                contexto.conteudoParaIA()
        );

        return summaryService.gerarResumo(request);
    }

    @PostMapping("/{id}/resumo/enviar")
    public ResponseEntity<String> enviarResumoPorEmail(@PathVariable String id) {

        try {
            emailSummaryService.enviarResumoPorEmail(id);

            return ResponseEntity.ok("Resumo enviado por e-mail com sucesso.");

        } catch (AiCommunicationException e) {
            return ResponseEntity
                    .status(503)
                    .body("Não foi possível gerar o resumo porque o serviço de IA está temporariamente indisponível.");
        }
    }

    @GetMapping("/briefing")
    public ResponseEntity<String> gerarBriefingDiario() {

        try {
            List<EmailResumoDTO> emails = emailService.listarEmails();

            List<DailyBriefingItem> itens = new ArrayList<>();

            int numero = 1;

            for (EmailResumoDTO emailResumo : emails) {

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
            }

            DailyBriefing briefing = dailyBriefingService.criarBriefing(itens);

            String texto = dailyBriefingFormatterService.formatar(briefing);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType("text/plain; charset=UTF-8"))
                    .body(texto);

        } catch (AiCommunicationException e) {
            return ResponseEntity
                    .status(503)
                    .body("Não foi possível gerar o briefing porque o serviço de IA está temporariamente indisponível.");
        }
    }

}