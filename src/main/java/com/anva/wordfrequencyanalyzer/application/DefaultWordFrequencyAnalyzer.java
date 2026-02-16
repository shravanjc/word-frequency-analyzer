package com.anva.wordfrequencyanalyzer.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import com.anva.wordfrequencyanalyzer.infra.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultWordFrequencyAnalyzer implements WordFrequencyAnalyzer {

    private final ApplicationProperties  applicationProperties;
    public static final String INVALID_INPUT_ERROR = "Invalid input. Cannot be null or empty";

    @Override
    public int calculateHighestFrequency(String text) {
        final Map<String, Integer> result = getWordFrequencyGrouping(text);
        return result.values().stream().max(Integer::compareTo).orElse(0);
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        return 0;
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {
        return List.of();
    }

    private Map<String, Integer> getWordFrequencyGrouping(String text) {
        assert text != null : INVALID_INPUT_ERROR;
        Map<String, Integer> result = new HashMap<>();

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
