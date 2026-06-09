package com.huseyin.footballanalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.key}")
    private String geminiApiKey;

    public String generateAiCommentary(String megaFootballData) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + geminiApiKey;

        String prompt = "Sen dünyanın en iyi futbol veri analisti ve spor yorumcususun. Sana bir maç öncesinde, " +
                "Sofascore API'sinden çektiğim iki takımın son maç listelerini ve sezon genelindeki maç başı derin istatistiklerini (xG gol beklentisi, şut, korner, kartlar) veriyorum. " +
                "Bu verileri bir yazılım gözüyle tara, iki takımın form durumunu ve taktiksel şablonunu çıkar. " +
                "Ardından bize şu başlıklar altında Türkçe profesyonel bir analiz sun:\n\n" +
                "1) FORM VE GÜNCEL MOMENTUM GRAFİĞİ (Son maç sonuçlarının analizi)\n" +
                "2) DERIN İSTATİSTİK VE xG KIYASLAMASI (Maç başı şut, gol beklentisi ve tehlike metrikleri)\n" +
                "3) TAKTİKSEL MAÇ ÖNGÖRÜSÜ (Tempo, oyun karakteri ve stratejik gidişat tahmini)\n\n" +
                "İşte ham veri seti:\n" + megaFootballData;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textBody = Map.of("text", prompt);
        Map<String, Object> parts = Map.of("parts", List.of(textBody));
        Map<String, Object> contents = Map.of("contents", List.of(parts));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contents, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            List<Map> candidates = (List<Map>) response.getBody().get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> responseParts = (List<Map>) content.get("parts");
            return responseParts.get(0).get("text").toString();
        } catch (Exception e) {
            return "[🤖 AI Analiz Hatası]: Veriler başarıyla toplandı fakat yapay zekaya gönderilirken hata oluştu: " + e.getMessage();
        }
    }
}