package com.huseyin.footballanalyzer.service;

import java.util.List;

// Thrown when Sofascore data is incomplete, so Gemini is never asked to analyse a match without data
public class MatchDataUnavailableException extends RuntimeException {

    public MatchDataUnavailableException(List<String> missingData) {
        super("Sofascore verisi alınamadı (" + String.join(", ", missingData) + "). "
                + "Eksik veriyle analiz yapılmadı, lütfen biraz sonra tekrar deneyin.");
    }
}
