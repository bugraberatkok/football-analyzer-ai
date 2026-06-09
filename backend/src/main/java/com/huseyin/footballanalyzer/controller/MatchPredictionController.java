package com.huseyin.footballanalyzer.controller;

import com.huseyin.footballanalyzer.dto.AnalysisResult;
import com.huseyin.footballanalyzer.service.MatchPredictionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predictions")
public class MatchPredictionController {
    private final MatchPredictionService predictionService;

    public MatchPredictionController(MatchPredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public AnalysisResult getPrediction(
            @RequestParam String leagueId,
            @RequestParam String homeTeamId,
            @RequestParam String homeTeamName,
            @RequestParam String awayTeamId,
            @RequestParam String awayTeamName) {
        return predictionService.getMatchPrediction(leagueId, homeTeamId, homeTeamName, awayTeamId, awayTeamName);
    }
}