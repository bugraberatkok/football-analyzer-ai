package com.huseyin.footballanalyzer.dto;

import lombok.Data;

@Data
public class MatchData {
    private String homeTeam;
    private String awayTeam;
    private String homeLastMatchesJson;
    private String awayLastMatchesJson;
    private String homeStatsJson;
    private String awayStatsJson;
}