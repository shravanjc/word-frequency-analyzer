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
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Slf4j
@Service
@Validated
public class DefaultWordFrequencyAnalyzer implements WordFrequencyAnalyzer {

    private final ApplicationProperties applicationProperties;

    @Override
    public int calculateHighestFrequency(final String text) {
        final Map<String, Integer> result = getWordFrequencyGrouping(text);
        return result.values().stream().max(Integer::compareTo).orElse(0);
    }

    @Override
    public int calculateFrequencyForWord(final String text, final String word) {
        final Map<String, Integer> result = getWordFrequencyGrouping(text);
        return result.getOrDefault(word.toLowerCase(), 0);
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(final String text, final int n) {
        final Map<String, Integer> result = getWordFrequencyGrouping(text, new LinkedHashMap<>());
        //Compare first by descending order of frequency, on tie use alphabetical sorting of word
        final Comparator<Map.Entry<String, Integer>> sortComparator = Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey());
        return result.entrySet().stream()
                .sorted(sortComparator)
                .limit(n)
                .map(entry -> new DefaultWordFrequency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getWordFrequencyGrouping(final String text) {
        Map<String, Integer> result = new HashMap<>();
        return getWordFrequencyGrouping(text, result);
    }

    private Map<String, Integer> getWordFrequencyGrouping(final String text, final Map<String, Integer> result) {
        //We avoid multiple lowerCase calls per word later on
        final String lowerCaseText = text.toLowerCase();
        for (String word : lowerCaseText.split(applicationProperties.separatorPattern())) {
            if (isNonWord(word)) continue;
            result.merge(word, 1, Integer::sum);
        }
        return result;
    }

    private boolean isNonWord(final String word) {
        //We expect the separator and word patterns to be mutually exclusive but still complete the charset
        //So we ignore words that do not match the configuration
        if (word.isBlank() || !word.matches(applicationProperties.wordPattern())) {
            log.debug("Word {} is not valid for configured pattern: {} ", word, applicationProperties.wordPattern());
            return true;
        }
        return false;
    }
}
