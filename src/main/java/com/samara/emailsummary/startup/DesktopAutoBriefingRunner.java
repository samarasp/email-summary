package com.samara.emailsummary.startup;

import com.samara.emailsummary.briefing.service.MultiMailboxBriefingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DesktopAutoBriefingRunner implements ApplicationRunner {

    private final MultiMailboxBriefingService multiMailboxBriefingService;
    private final ApplicationContext applicationContext;

    public DesktopAutoBriefingRunner(
            MultiMailboxBriefingService multiMailboxBriefingService,
            ApplicationContext applicationContext
    ) {
        this.multiMailboxBriefingService = multiMailboxBriefingService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(">>> DesktopAutoBriefingRunner iniciado");
        if (!args.containsOption("desktop-auto-briefing")) {
            return;
        }

        if (!args.containsOption("desktop-auto-briefing")) {
            System.out.println(">>> Argumento desktop-auto-briefing NÃO encontrado");
            return;
        }

        System.out.println(">>> Argumento desktop-auto-briefing encontrado");

        try {
            System.out.println();
            System.out.println("========================================");
            System.out.println("Email Summary");
            System.out.println("Gerando Daily Briefing Inteligente...");
            System.out.println("========================================");
            System.out.println();

            multiMailboxBriefingService.enviarBriefings();

            System.out.println();
            System.out.println("========================================");
            System.out.println("Resumo(s) enviado(s) com sucesso.");
            System.out.println("Encerrando aplicacao...");
            System.out.println("========================================");
            System.out.println();

            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);

        } catch (Exception e) {
            System.err.println();
            System.err.println("========================================");
            System.err.println("Nao foi possivel enviar o(s) resumo(s).");
            System.err.println("Motivo: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();

            int exitCode = SpringApplication.exit(applicationContext, () -> 1);
            System.exit(exitCode);
        }
    }
}