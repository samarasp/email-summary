package com.samara.emailsummary.briefing.dto;

import java.util.List;

public record BriefingAnalysis(
        List<BriefingEmailItem> emails
) {
}