package com.samara.emailsummary.briefing.mapper;

import com.samara.emailsummary.briefing.dto.BriefingAnalysis;
import com.samara.emailsummary.briefing.dto.BriefingClassification;
import com.samara.emailsummary.briefing.dto.DailyBriefing;
import com.samara.emailsummary.briefing.dto.DailyBriefingItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BriefingMapper {

    public DailyBriefing toDailyBriefing(BriefingAnalysis analysis) {
        List<DailyBriefingItem> itens = mapearItens(analysis);

        return new DailyBriefing(
                LocalDate.now(),
                LocalTime.now(),
                itens.size(),
                contarPorClassificacao(itens, BriefingClassification.EXIGE_ACAO),
                contarPorClassificacao(itens, BriefingClassification.ACOMPANHAR),
                contarPorClassificacao(itens, BriefingClassification.INFORMATIVO),
                itens
        );
    }

    private List<DailyBriefingItem> mapearItens(BriefingAnalysis analysis) {
        AtomicInteger contador = new AtomicInteger(1);

        return analysis.emails()
                .stream()
                .map(item -> new DailyBriefingItem(
                        contador.getAndIncrement(),
                        item.classificacao(),
                        item.remetente(),
                        item.assunto(),
                        item.resumo(),
                        item.proximasAcoes()
                ))
                .toList();
    }

    private int contarPorClassificacao(
            List<DailyBriefingItem> itens,
            BriefingClassification classificacao
    ) {
        return (int) itens.stream()
                .filter(item -> item.classificacao() == classificacao)
                .count();
    }
}