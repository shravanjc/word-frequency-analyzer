package com.anva.wordfrequencyanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WordFrequencyAnalyzerApplication {
    static void main(String[] args) {
        SpringApplication.run(WordFrequencyAnalyzerApplication.class, args);
    }
}
