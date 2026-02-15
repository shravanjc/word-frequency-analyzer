package com.anva.wordfrequencyanalyzer.domain;

import lombok.Value;

@Value
public class DefaultWordFrequency implements WordFrequency {
    String word;
    int frequency;
}
