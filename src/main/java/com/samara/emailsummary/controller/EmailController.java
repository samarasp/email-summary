package com.samara.emailsummary.controller;

import com.samara.emailsummary.dto.EmailDetalheDTO;
import com.samara.emailsummary.dto.EmailResumoDTO;
import com.samara.emailsummary.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
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
}