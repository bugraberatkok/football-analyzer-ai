package com.huseyin.footballanalyzer.facade;

import com.huseyin.footballanalyzer.dto.MatchData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RealFootballDataFacade implements FootballDataFacade {

    // SportAPI on RapidAPI serves Sofascore data under Sofascore's own /api/v1 paths
    private static final String API_HOST = "sportapi7.p.rapidapi.com";
    private static final String BASE_URL = "https://" + API_HOST + "/api/v1";

    private final RestTemplate restTemplate = new RestTemplate();

    // Current season per league, cached so an analysis only spends the four channel requests
    private final Map<String, String> currentSeasonIds = new ConcurrentHashMap<>();

    @Value("${rapidapi.key}")
    private String apiKey;

    @Override
    public MatchData fetchAndPrepareMatchData(String leagueId, String homeTeamId, String homeTeamName, String awayTeamId, String awayTeamName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", API_HOST);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        MatchData matchData = new MatchData();
        matchData.setHomeTeam(homeTeamName);
        matchData.setAwayTeam(awayTeamName);

        // Channel 1: Home team — last matches
        try {
            String url = BASE_URL + "/team/" + homeTeamId + "/events/last/0";
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setHomeLastMatchesJson(res.getBody());
        } catch (Exception e) {
            matchData.setHomeLastMatchesJson("{ 'error': 'Maç listesi alınamadı' }");
        }

        // Channel 2: Away team — last matches
        try {
            String url = BASE_URL + "/team/" + awayTeamId + "/events/last/0";
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setAwayLastMatchesJson(res.getBody());
        } catch (Exception e) {
            matchData.setAwayLastMatchesJson("{ 'error': 'Match list unavailable' }");
        }

        // Channel 3: Home team — season statistics
        try {
            String url = BASE_URL + "/team/" + homeTeamId + "/unique-tournament/" + leagueId
                    + "/season/" + currentSeasonId(leagueId, entity) + "/statistics/overall";
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setHomeStatsJson(res.getBody());
        } catch (Exception e) {
            matchData.setHomeStatsJson("{ 'error': 'Statistics unavailable' }");
        }

        // Channel 4: Away team — season statistics
        try {
            String url = BASE_URL + "/team/" + awayTeamId + "/unique-tournament/" + leagueId
                    + "/season/" + currentSeasonId(leagueId, entity) + "/statistics/overall";
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            matchData.setAwayStatsJson(res.getBody());
        } catch (Exception e) {
            matchData.setAwayStatsJson("{ 'error': 'Statistics unavailable' }");
        }

        return matchData;
    }

    // The league's season list is newest first, so the first entry is the running season
    private String currentSeasonId(String leagueId, HttpEntity<String> entity) {
        return currentSeasonIds.computeIfAbsent(leagueId, id -> {
            String url = BASE_URL + "/unique-tournament/" + id + "/seasons";
            ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            List<Map> seasons = (List<Map>) res.getBody().get("seasons");
            return String.valueOf(seasons.get(0).get("id"));
        });
    }
}
