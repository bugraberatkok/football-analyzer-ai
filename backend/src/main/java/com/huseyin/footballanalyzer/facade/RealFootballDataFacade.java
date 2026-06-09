package com.huseyin.footballanalyzer.facade;

import com.huseyin.footballanalyzer.dto.MatchData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RealFootballDataFacade implements FootballDataFacade {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${rapidapi.key}")
    private String apiKey;

    @Override
    public MatchData fetchAndPrepareMatchData(String leagueId, String homeTeamId, String homeTeamName, String awayTeamId, String awayTeamName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", "sofascore.p.rapidapi.com");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        MatchData matchData = new MatchData();
        matchData.setHomeTeam(homeTeamName);
        matchData.setAwayTeam(awayTeamName);

        // Channel 1: Home team — last matches
        try {
            String url = "https://sofascore.p.rapidapi.com/teams/get-last-matches?teamId=" + homeTeamId;
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setHomeLastMatchesJson(res.getBody());
        } catch (Exception e) {
            matchData.setHomeLastMatchesJson("{ 'error': 'Maç listesi alınamadı' }");
        }

        // Channel 2: Away team — last matches
        try {
            String url = "https://sofascore.p.rapidapi.com/teams/get-last-matches?teamId=" + awayTeamId;
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setAwayLastMatchesJson(res.getBody());
        } catch (Exception e) {
            matchData.setAwayLastMatchesJson("{ 'error': 'Match list unavailable' }");
        }

        // Channel 3: Home team — season statistics
        try {
            String url = "https://sofascore.p.rapidapi.com/teams/get-statistics?teamId=" + homeTeamId;
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setHomeStatsJson(res.getBody());
        } catch (Exception e) {
            matchData.setHomeStatsJson("{ 'error': 'Statistics unavailable' }");
        }

        // Channel 4: Away team — season statistics
        try {
            String url = "https://sofascore.p.rapidapi.com/teams/get-statistics?teamId=" + awayTeamId;
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setAwayStatsJson(res.getBody());
        } catch (Exception e) {
            matchData.setAwayStatsJson("{ 'error': 'Statistics unavailable' }");
        }

        return matchData;
    }
}