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

    // Constructor injection for loose coupling — all dependencies injected by Spring IoC container
    public MatchPredictionService(FootballDataFacade footballDataFacade,
                                  MatchAnalysisStrategy matchAnalysisStrategy,
                                  AiService aiService) {
        this.footballDataFacade = footballDataFacade;
        this.matchAnalysisStrategy = matchAnalysisStrategy;
        this.aiService = aiService;
    }

    public AnalysisResult getMatchPrediction(String leagueId, String homeTeamId, String homeTeamName, String awayTeamId, String awayTeamName) {
        // Step 1: Fetch raw match data from external API via Facade
        MatchData matchData = footballDataFacade.fetchAndPrepareMatchData(leagueId, homeTeamId, homeTeamName, awayTeamId, awayTeamName);

        // Without real data Gemini would invent the numbers, so stop here instead
        if (!matchData.getMissingData().isEmpty()) {
            throw new MatchDataUnavailableException(matchData.getMissingData());
        }

        // Step 2: Process raw data using the configured analysis strategy
        AnalysisResult analysisResult = matchAnalysisStrategy.analyze(matchData);

        // Step 3: Send aggregated data to the AI service and retrieve the generated commentary
        String aiCommentary = aiService.generateAiCommentary(analysisResult.getRawDataSummary());

        // Step 4: Replace the raw data summary with the AI-generated analysis report
        analysisResult.setRawDataSummary(aiCommentary);

        return analysisResult;
    }
}