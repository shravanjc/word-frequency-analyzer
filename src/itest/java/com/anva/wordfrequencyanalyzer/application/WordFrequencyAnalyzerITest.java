package com.anva.wordfrequencyanalyzer.application;

import java.util.List;

import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer.INVALID_LIMIT_ERROR;
import static com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer.INVALID_TEXT_ERROR;
import static com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer.INVALID_WORD_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WordFrequencyAnalyzerITest {

    @Autowired
    private WordFrequencyAnalyzer wordFrequencyAnalyzer;

    @Test
    void calculateFrequencyForWordInvalidTest() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> wordFrequencyAnalyzer.calculateFrequencyForWord(null, "test"));
        assertThat(exception.getMessage()).contains(INVALID_TEXT_ERROR);

        exception = assertThrows(ConstraintViolationException.class, () -> wordFrequencyAnalyzer.calculateFrequencyForWord(" ", null));
        assertThat(exception.getMessage()).contains(INVALID_WORD_ERROR);
    }

    @Test
    void calculateHighestFrequencyInvalidTest() {
        final ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> wordFrequencyAnalyzer.calculateHighestFrequency(null));
        assertThat(exception.getMessage()).contains(INVALID_TEXT_ERROR);
    }

    @Test
    void calculateMostFrequentNWordsTest() {
        final List<WordFrequency> wordFrequencies = wordFrequencyAnalyzer.calculateMostFrequentNWords("test most frequent N words test ", 2);

        assertThat(wordFrequencies.size()).isEqualTo(2);

        final WordFrequency firstWordFreq = wordFrequencies.getFirst();
        assertThat(firstWordFreq.getFrequency()).isEqualTo(2);
        assertThat(firstWordFreq.getWord()).isEqualTo("test");

        final WordFrequency secondWordFreq = wordFrequencies.getLast();
        assertThat(secondWordFreq.getFrequency()).isEqualTo(1);
        assertThat(secondWordFreq.getWord()).isEqualTo("frequent");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/calculateMostFrequentNWords_invalid.csv", numLinesToSkip = 1)
    void calculateMostFrequentNWordsInvalidTest(String text, int limit) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> wordFrequencyAnalyzer.calculateMostFrequentNWords(text, limit));
        assertThat(exception.getMessage()).contains(INVALID_LIMIT_ERROR);
    }
}
