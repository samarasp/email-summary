package com.samara.emailsummary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailSummaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailSummaryApplication.class, args);
    }
}