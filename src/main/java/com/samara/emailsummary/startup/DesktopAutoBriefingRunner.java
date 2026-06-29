package com.samara.emailsummary.startup;

import com.samara.emailsummary.briefing.service.DailyBriefingEmailService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DesktopAutoBriefingRunner implements ApplicationRunner {

    private final DailyBriefingEmailService dailyBriefingEmailService;
    private final ApplicationContext applicationContext;

    public DesktopAutoBriefingRunner(
            DailyBriefingEmailService dailyBriefingEmailService,
            ApplicationContext applicationContext
    ) {
        this.dailyBriefingEmailService = dailyBriefingEmailService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption("desktop-auto-briefing")) {
            return;
        }

        try {
            System.out.println();
            System.out.println("========================================");
            System.out.println("Email Summary");
            System.out.println("Gerando Daily Briefing Inteligente...");
            System.out.println("========================================");
            System.out.println();

            dailyBriefingEmailService.enviarBriefingDiario();

            System.out.println();
            System.out.println("========================================");
            System.out.println("Resumo enviado com sucesso.");
            System.out.println("Encerrando aplicacao...");
            System.out.println("========================================");
            System.out.println();

            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);

        } catch (Exception e) {
            System.err.println();
            System.err.println("========================================");
            System.err.println("Nao foi possivel enviar o resumo.");
            System.err.println("Motivo: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();

            int exitCode = SpringApplication.exit(applicationContext, () -> 1);
            System.exit(exitCode);
        }
    }
}