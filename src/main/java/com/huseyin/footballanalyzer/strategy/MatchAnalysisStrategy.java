package com.huseyin.footballanalyzer.strategy;

import com.huseyin.footballanalyzer.dto.MatchData;
import com.huseyin.footballanalyzer.dto.AnalysisResult;

public interface MatchAnalysisStrategy {
    AnalysisResult analyze(MatchData matchData);
}