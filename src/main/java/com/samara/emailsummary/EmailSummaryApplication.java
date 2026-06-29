package com.samara.emailsummary;

import com.samara.emailsummary.config.EmailSummaryProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(EmailSummaryProperties.class)
public class EmailSummaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailSummaryApplication.class, args);
    }
}