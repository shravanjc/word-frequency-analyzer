package com.anva.wordfrequencyanalyzer.api;

import java.util.List;

import com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WordFrequencyController {
    private final WordFrequencyAnalyzer wordFrequencyAnalyzer;

    @PostMapping("/highest-frequency")
    public int calculateHighestFrequency(@RequestBody String text) {
        return wordFrequencyAnalyzer.calculateHighestFrequency(text);
    }

    @PostMapping("/frequency/{word}")
    public int calculateFrequencyForWord(String text, @PathVariable String word) {
        return wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
    }

    @PostMapping("/top-frequency/{limit}")
    public List<WordFrequency> calculateMostFrequentNWords(String text, @PathVariable int limit) {
        return wordFrequencyAnalyzer.calculateMostFrequentNWords(text, limit);
    }
}

