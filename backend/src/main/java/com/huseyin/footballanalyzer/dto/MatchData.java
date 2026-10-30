package com.huseyin.footballanalyzer.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatchData {
    private String homeTeam;
    private String awayTeam;
    private String homeLastMatchesJson;
    private String awayLastMatchesJson;
    private String homeStatsJson;
    private String awayStatsJson;

    // Channels that could not be fetched; the analysis is not run while any are missing
    private List<String> missingData = new ArrayList<>();
}