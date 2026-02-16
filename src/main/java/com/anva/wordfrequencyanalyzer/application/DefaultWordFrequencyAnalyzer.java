package com.anva.wordfrequencyanalyzer.application;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.anva.wordfrequencyanalyzer.domain.DefaultWordFrequency;
import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import com.anva.wordfrequencyanalyzer.infra.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultWordFrequencyAnalyzer implements WordFrequencyAnalyzer {

    public static final String INVALID_TEXT_ERROR = "Input text is required";
    public static final String INVALID_WORD_ERROR = "Input word is required";
    public static final String INVALID_LIMIT_ERROR = "Input limit should be greater than 0";

    private final ApplicationProperties applicationProperties;

    @Override
    public int calculateHighestFrequency(String text) {
        final Map<String, Integer> result = getWordFrequencyGrouping(text);
        return result.values().stream().max(Integer::compareTo).orElse(0);
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        if (word == null) {
            throw new IllegalArgumentException(INVALID_WORD_ERROR);
        }
        final Map<String, Integer> result = getWordFrequencyGrouping(text);
        return result.getOrDefault(word, 0);
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(INVALID_LIMIT_ERROR);
        }
        final Map<String, Integer> result = getWordFrequencyGrouping(text, new LinkedHashMap<>());
        final Comparator<Map.Entry<String, Integer>> sortComparator = Map.Entry.<String, Integer>comparingByValue().reversed();
        return result.entrySet().stream()
                .sorted(sortComparator)
                .limit(n)
                .map(entry -> new DefaultWordFrequency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getWordFrequencyGrouping(String text) {
        Map<String, Integer> result = new HashMap<>();
        return getWordFrequencyGrouping(text, result);
    }

    private Map<String, Integer> getWordFrequencyGrouping(String text, final Map<String, Integer> result) {
        if (text == null) {
            throw new IllegalArgumentException(INVALID_TEXT_ERROR);
        }
        //We avoid multiple lowerCase calls per word later on
        final String lowerCaseText = text.toLowerCase();

        for (String word : lowerCaseText.split(applicationProperties.separatorPattern())) {
            //We expect the separator and word patterns to be mutually exclusive but still complete the charset
            //So we ignore words that do not match the configuration
            if (word.isBlank() || !word.matches(applicationProperties.wordPattern())) {
                log.debug("Word {} is not valid for configured pattern: {} ", word, applicationProperties.wordPattern());
                continue;
            }
            result.merge(word, 1, Integer::sum);
        }
        return result;
    }
}
