package com.anva.wordfrequencyanalyzer.api;

import java.util.List;

import com.anva.wordfrequencyanalyzer.application.WordFrequencyService;
import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WordFrequencyController {
    private final WordFrequencyService wordFrequencyService;

    @PostMapping("/highest-frequency")
    public int calculateHighestFrequency(String text) {
        return wordFrequencyService.calculateHighestFrequency(text);
    }

    @PostMapping("/frequency/{word}")
    public int calculateFrequencyForWord(String text, @PathVariable String word) {
        return wordFrequencyService.calculateFrequencyForWord(text, word);
    }

    @PostMapping("/top-frequency/{limit}")
    public List<WordFrequency> calculateMostFrequentNWords(String text, @PathVariable int limit) {
        return wordFrequencyService.calculateMostFrequentNWords(text, limit);
    }
}

