package com.samara.emailsummary.controller;

import com.samara.emailsummary.ai.dto.SummaryRequest;
import com.samara.emailsummary.ai.dto.SummaryResponse;
import com.samara.emailsummary.ai.exception.AiCommunicationException;
import com.samara.emailsummary.ai.service.SummaryService;
import com.samara.emailsummary.briefing.context.EmailContext;
import com.samara.emailsummary.briefing.context.EmailContextBuilder;
import com.samara.emailsummary.briefing.service.DailyBriefingEmailService;
import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.service.EmailService;
import com.samara.emailsummary.service.EmailSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;
    private final SummaryService summaryService;
    private final EmailSummaryService emailSummaryService;
    private final EmailContextBuilder emailContextBuilder;
    private final DailyBriefingEmailService dailyBriefingEmailService;

    public EmailController(
            EmailService emailService,
            SummaryService summaryService,
            EmailSummaryService emailSummaryService,
            EmailContextBuilder emailContextBuilder,
            DailyBriefingEmailService dailyBriefingEmailService
    ) {
        this.emailService = emailService;
        this.summaryService = summaryService;
        this.emailSummaryService = emailSummaryService;
        this.emailContextBuilder = emailContextBuilder;
        this.dailyBriefingEmailService = dailyBriefingEmailService;
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
    public SummaryResponse gerarBriefingDiario() {
        return dailyBriefingEmailService.gerarBriefingInteligente();
    }
}