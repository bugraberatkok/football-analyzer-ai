package com.huseyin.footballanalyzer.strategy;

import com.huseyin.footballanalyzer.dto.MatchData;
import com.huseyin.footballanalyzer.dto.AnalysisResult;
import org.springframework.stereotype.Component;

@Component
public class BasicFormAnalysisStrategy implements MatchAnalysisStrategy {

    @Override
    public AnalysisResult analyze(MatchData matchData) {
        AnalysisResult result = new AnalysisResult();
        result.setDominantTeam("AI Kararı");
        result.setMatchPace("Dinamik Analiz");
        result.setTotalExpectedGoals(0.0);

        // Merge all four JSON data sources into a single structured prompt body for the AI service
        String megaPromptBody = "=== EV SAHİBİ (" + matchData.getHomeTeam() + ") SON MAÇLARI ===\n" + matchData.getHomeLastMatchesJson() + "\n\n" +
                "=== DEPLASMAN (" + matchData.getAwayTeam() + ") SON MAÇLARI ===\n" + matchData.getAwayLastMatchesJson() + "\n\n" +
                "=== EV SAHİBİ SEZON GENELİ DERİN İSTATİSTİKLERİ ===\n" + matchData.getHomeStatsJson() + "\n\n" +
                "=== DEPLASMAN SEZON GENELİ DERİN İSTATİSTİKLERİ ===\n" + matchData.getAwayStatsJson();

        result.setRawDataSummary(megaPromptBody);
        return result;
    }
}