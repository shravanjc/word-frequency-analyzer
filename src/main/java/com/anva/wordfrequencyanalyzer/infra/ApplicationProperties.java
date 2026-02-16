package com.anva.wordfrequencyanalyzer.infra;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public record ApplicationProperties(String wordPattern, String separatorPattern) {
}
