package com.huseyin.footballanalyzer.dto;

import lombok.Data;

@Data
public class AnalysisResult {
    private String dominantTeam;
    private double totalExpectedGoals;
    private String matchPace;
    private String rawDataSummary;
}