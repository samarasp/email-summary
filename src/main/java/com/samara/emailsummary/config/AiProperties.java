package com.samara.emailsummary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiProperties {

    @Value("${email.summary.ai.enabled:false}")
    private boolean enabled;

    @Value("${email.summary.ai.provider:gemini}")
    private String provider;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.api.url:}")
    private String geminiApiUrl;

    public boolean isEnabled() {
        return enabled;
    }

    public String getProvider() {
        return provider;
    }

    public String getGeminiApiKey() {
        return geminiApiKey;
    }

    public String getGeminiApiUrl() {
        return geminiApiUrl;
    }
}