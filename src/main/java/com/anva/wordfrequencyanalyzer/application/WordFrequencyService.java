package com.anva.wordfrequencyanalyzer.application;

import java.util.List;

import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import com.anva.wordfrequencyanalyzer.domain.WordFrequencyAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordFrequencyService {

    private final WordFrequencyAnalyzer wordFrequencyAnalyzer;

    public int calculateHighestFrequency(String text) {
        return wordFrequencyAnalyzer.calculateHighestFrequency(text);
    }

    public int calculateFrequencyForWord(String text, String word) {
        return wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
    }

    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        return wordFrequencyAnalyzer.calculateMostFrequentNWords(text, n);
    }

    public List<WordFrequency> analyzeText(String text) {
        // Assuming a default of 10 for 'n' (most frequent words)
        return wordFrequencyAnalyzer.calculateMostFrequentNWords(text, 10);
    }
}
