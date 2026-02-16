package com.anva.wordfrequencyanalyzer.application;

import java.util.List;

import com.anva.wordfrequencyanalyzer.domain.WordFrequency;

public interface WordFrequencyAnalyzer {
    int calculateHighestFrequency(String text);

    int calculateFrequencyForWord(String text, String word);

    List<WordFrequency> calculateMostFrequentNWords(String text, int n);
}
