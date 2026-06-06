package com.huseyin.footballanalyzer.facade;

import com.huseyin.footballanalyzer.dto.MatchData;

public interface FootballDataFacade {
    MatchData fetchAndPrepareMatchData(String leagueId, String homeTeamId, String homeTeamName, String awayTeamId, String awayTeamName);
}