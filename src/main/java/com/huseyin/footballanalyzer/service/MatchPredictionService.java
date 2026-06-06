package com.huseyin.footballanalyzer.service;

import com.huseyin.footballanalyzer.dto.MatchData;
import com.huseyin.footballanalyzer.dto.AnalysisResult;
import com.huseyin.footballanalyzer.facade.FootballDataFacade;
import com.huseyin.footballanalyzer.strategy.MatchAnalysisStrategy;
import org.springframework.stereotype.Service;

@Service
public class MatchPredictionService {

    private final FootballDataFacade footballDataFacade;
    private final MatchAnalysisStrategy matchAnalysisStrategy;
    private final AiService aiService;

    // Tüm mimari bileşenleri gevşek bağlı (loose coupling) olarak enjekte ediyoruz
    public MatchPredictionService(FootballDataFacade footballDataFacade,
                                  MatchAnalysisStrategy matchAnalysisStrategy,
                                  AiService aiService) {
        this.footballDataFacade = footballDataFacade;
        this.matchAnalysisStrategy = matchAnalysisStrategy;
        this.aiService = aiService;
    }

    public AnalysisResult getMatchPrediction(String leagueId, String homeTeamId, String homeTeamName, String awayTeamId, String awayTeamName) {
        // 1. Gerçek API'den son yılların maç verilerini çek
        MatchData matchData = footballDataFacade.fetchAndPrepareMatchData(leagueId, homeTeamId, homeTeamName, awayTeamId, awayTeamName);

        // 2. Verileri basit istatistiksel stratejimizle süzgeçten geçirip özet oluştur
        AnalysisResult analysisResult = matchAnalysisStrategy.analyze(matchData);

        // 3. Oluşan bu özeti Gerçek Yapay Zekaya gönder ve insan dilindeki yorumu al
        String aiCommentary = aiService.generateAiCommentary(analysisResult.getRawDataSummary());

        // 4. UI ekranındaki siyah kutuya algoritma özeti yerine gerçek AI yorumunu basıyoruz
        analysisResult.setRawDataSummary(aiCommentary);

        return analysisResult;
    }
}