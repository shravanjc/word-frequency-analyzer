package com.anva.wordfrequencyanalyzer.application;

import java.util.List;

import com.anva.wordfrequencyanalyzer.domain.WordFrequency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface WordFrequencyAnalyzer {

    String INVALID_TEXT_ERROR = "Input text is required";
    String INVALID_WORD_ERROR = "Input word is required";
    String INVALID_LIMIT_ERROR = "Input limit should be greater than 0";

    int calculateHighestFrequency(@NotNull(message = INVALID_TEXT_ERROR) final String text);

    int calculateFrequencyForWord(@NotNull(message = INVALID_TEXT_ERROR) final String text, @NotNull(message = INVALID_WORD_ERROR) final String word);

    List<WordFrequency> calculateMostFrequentNWords(@NotNull(message = INVALID_TEXT_ERROR) final String text, @Min(value = 1, message = INVALID_LIMIT_ERROR) final int n);
}
