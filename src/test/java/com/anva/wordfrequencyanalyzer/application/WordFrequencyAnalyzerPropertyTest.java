package com.anva.wordfrequencyanalyzer.application;

import com.anva.wordfrequencyanalyzer.infra.ApplicationProperties;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;

import static org.assertj.core.api.Assertions.assertThat;

class WordFrequencyAnalyzerPropertyTest {

    private final ApplicationProperties applicationProperties =
            new ApplicationProperties("^[a-zA-Z]+$", "[^a-zA-Z]+");
    private final WordFrequencyAnalyzer wordFrequencyAnalyzer =
            new DefaultWordFrequencyAnalyzer(applicationProperties);

    // Invariant 1: result is never negative for any input
    @Property
    void frequencyIsAlwaysNonNegative(
            @ForAll @AlphaChars @StringLength(min = 1) String word,
            @ForAll String text) {
        assertThat(wordFrequencyAnalyzer.calculateFrequencyForWord(text, word))
                .isGreaterThanOrEqualTo(0);
    }

    // Invariant 2: casing of the text has no effect since it is lowercased internally
    @Property
    void textCasingDoesNotAffectResult(
            @ForAll @AlphaChars @StringLength(min = 1) String word,
            @ForAll @AlphaChars @StringLength(min = 1) String text) {
        int fromLower = wordFrequencyAnalyzer.calculateFrequencyForWord(text.toLowerCase(), word);
        int fromUpper = wordFrequencyAnalyzer.calculateFrequencyForWord(text.toUpperCase(), word);
        assertThat(fromLower).isEqualTo(fromUpper);
    }

    // Invariant 3: appending a word to any text increases its frequency by exactly 1.
    @Property
    void appendingWordIncreasesFrequencyByOne(
            @ForAll @AlphaChars @StringLength(min = 1) String word,
            @ForAll String text) {
        int before = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word);
        int after = wordFrequencyAnalyzer.calculateFrequencyForWord(text + " " + word, word);
        int afterForLowerCase = wordFrequencyAnalyzer.calculateFrequencyForWord(text + " " + word, word.toLowerCase());
        assertThat(after).isEqualTo(before + 1);
        assertThat(afterForLowerCase).isEqualTo(after);
    }

    // Invariant 4: no word can appear more often than the most frequent word in the text
    @Property
    void frequencyNeverExceedsHighestFrequency(
            @ForAll @AlphaChars @StringLength(min = 1) String word,
            @ForAll @AlphaChars @StringLength(min = 1) String text) {
        int freq = wordFrequencyAnalyzer.calculateFrequencyForWord(text, word.toLowerCase());
        int highest = wordFrequencyAnalyzer.calculateHighestFrequency(text);
        assertThat(freq).isLessThanOrEqualTo(highest);
    }
}
