package com.anva.wordfrequencyanalyzer.api;

import java.util.List;

import com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/frequency")
public class WordFrequencyController {
    private final WordFrequencyAnalyzer wordFrequencyAnalyzer;

    @PostMapping("/highest")
    public ResponseEntity<ApiResponse<Integer>> calculateHighestFrequency(@RequestBody String text) {
        final int highestFrequency = wordFrequencyAnalyzer.calculateHighestFrequency(text);
        return ResponseEntity.ok(ApiResponse.success(highestFrequency));
    }

    @PostMapping("/by-word/{word}")
    public ResponseEntity<ApiResponse<Integer>> calculateFrequencyForWord(@RequestBody String text, @PathVariable String word) {
        final int frequencyForWord = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
        return ResponseEntity.ok(ApiResponse.success(frequencyForWord));
    }

    @PostMapping("/by-limit/{limit}")
    public ResponseEntity<ApiResponse<List<WordFrequency>>> calculateMostFrequentNWords(@RequestBody String text, @PathVariable int limit) {
        final List<WordFrequency> wordFrequencies = wordFrequencyAnalyzer.calculateMostFrequentNWords(text, limit);
        return ResponseEntity.ok(ApiResponse.success(wordFrequencies));
    }
}

