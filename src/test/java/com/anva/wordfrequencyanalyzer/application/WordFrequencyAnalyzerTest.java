package com.anva.wordfrequencyanalyzer.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.domain.DefaultWordFrequency;
import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import com.anva.wordfrequencyanalyzer.infra.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_LIMIT_ERROR;
import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_TEXT_ERROR;
import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_WORD_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordFrequencyAnalyzerTest {

    //Arrange
    private final ApplicationProperties applicationProperties = new ApplicationProperties("^[a-zA-Z]+$", "[^a-zA-Z]+");
    private final WordFrequencyAnalyzer wordFrequencyAnalyzer = new DefaultWordFrequencyAnalyzer(applicationProperties);

    @ParameterizedTest
    @CsvFileSource(resources = "/calculateHighestFrequency_valid.csv", numLinesToSkip = 1)
    void calculateHighestFrequencyTest(String text, int expectedFreq) {
        //Act
        final int highestFrequency = wordFrequencyAnalyzer.calculateHighestFrequency(text);
        //Assert
        assertThat(highestFrequency).isEqualTo(expectedFreq);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/calculateFrequencyForWord_valid.csv", numLinesToSkip = 1)
    void calculateFrequencyForWordTest(String text, String word, int expectedFreq) {
        final int highestFrequency = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
        assertThat(highestFrequency).isEqualTo(expectedFreq);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/calculateMostFrequentNWords_valid.csv", numLinesToSkip = 1)
    void calculateMostFrequentNWordsTest(String text, int limit, String expectedWordAndFreq) {
        final List<WordFrequency> wordFrequencies = wordFrequencyAnalyzer.calculateMostFrequentNWords(text, limit);
        final List<WordFrequency> expectedWordFrequencies = parseWordFrequency(expectedWordAndFreq);
        assertThat(wordFrequencies).containsExactlyInAnyOrderElementsOf(expectedWordFrequencies);
    }

    private List<WordFrequency> parseWordFrequency(String text) {
        return Arrays.stream(text.split(";")).map(wordFrequencyString -> {
            final String[] wordFrequency = wordFrequencyString.split(":");
            final String word = wordFrequency[0];
            final int frequency = Integer.parseInt(wordFrequency[1]);
            return new DefaultWordFrequency(word, frequency);
        }).collect(Collectors.toUnmodifiableList());
    }
}
