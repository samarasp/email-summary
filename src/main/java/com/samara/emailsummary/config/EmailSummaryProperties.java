package com.samara.emailsummary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email.summary")
public class EmailSummaryProperties {

    private String destination;
    private int maxEmailsPerRun;
    private int contextEmails;
    private int contextDays;
    private String language;
    private String schedule;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getMaxEmailsPerRun() {
        return maxEmailsPerRun;
    }

    public void setMaxEmailsPerRun(int maxEmailsPerRun) {
        this.maxEmailsPerRun = maxEmailsPerRun;
    }

    public int getContextEmails() {
        return contextEmails;
    }

    public void setContextEmails(int contextEmails) {
        this.contextEmails = contextEmails;
    }

    public int getContextDays() {
        return contextDays;
    }

    public void setContextDays(int contextDays) {
        this.contextDays = contextDays;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}