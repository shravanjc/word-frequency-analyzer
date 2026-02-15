package com.anva.wordfrequencyanalyzer.domain;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DefaultWordFrequencyAnalyzer implements WordFrequencyAnalyzer {

    @Override
    public int calculateHighestFrequency(String text) {
        return 0;
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        return 0;
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        return List.of();
    }
}
