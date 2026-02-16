package com.anva.wordfrequencyanalyzer.domain;

import com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.infra.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_INPUT_ERROR;
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

    @Test
    void calculateHighestFrequencyInvalidTest() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> wordFrequencyAnalyzer.calculateHighestFrequency(null));
        assertThat(exception.getMessage()).isEqualTo(INVALID_INPUT_ERROR);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/calculateFrequencyForWord_valid.csv", numLinesToSkip = 1)
    void calculateFrequencyForWordTest(String text, String word, int expectedFreq) {
        final int highestFrequency = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
        assertThat(highestFrequency).isEqualTo(expectedFreq);
    }

    @Test
    void calculateFrequencyForWordInvalidTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> wordFrequencyAnalyzer.calculateFrequencyForWord(null, "test"));
        assertThat(exception.getMessage()).isEqualTo(INVALID_INPUT_ERROR);

        exception = assertThrows(IllegalArgumentException.class, () -> wordFrequencyAnalyzer.calculateFrequencyForWord(" ", null));
        assertThat(exception.getMessage()).isEqualTo(INVALID_INPUT_ERROR);
    }
}
