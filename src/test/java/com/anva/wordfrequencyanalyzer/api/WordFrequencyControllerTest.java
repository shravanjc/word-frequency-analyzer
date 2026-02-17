package com.anva.wordfrequencyanalyzer.api;

import java.util.List;
import java.util.Set;
import java.util.random.RandomGenerator;

import com.anva.wordfrequencyanalyzer.application.WordFrequencyAnalyzer;
import com.anva.wordfrequencyanalyzer.domain.DefaultWordFrequency;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.anva.wordfrequencyanalyzer.api.ControllerExceptionHandler.GENERIC_ERROR_MESSAGE;
import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_LIMIT_ERROR;
import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_TEXT_ERROR;
import static com.anva.wordfrequencyanalyzer.application.DefaultWordFrequencyAnalyzer.INVALID_WORD_ERROR;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordFrequencyController.class)
class WordFrequencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WordFrequencyAnalyzer wordFrequencyAnalyzer;

    @MockitoSpyBean
    private ControllerExceptionHandler exceptionHandler;

    @Test
    @DisplayName("POST /frequency/highest - Success")
    void calculateHighestFrequency() throws Exception {
        //Arrange
        final int frequency = RandomGenerator.getDefault().nextInt();
        when(wordFrequencyAnalyzer.calculateHighestFrequency(anyString()))
                .thenReturn(frequency);

        //Act and Assert
        mockMvc.perform(post("/frequency/highest")
                        .content("hello world"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data").value(frequency));
    }

    @Test
    @DisplayName("POST /frequency/highest - Failure - Empty request body")
    void calculateHighestFrequencyInvalidTest() throws Exception {
        //Arrange
        final ArgumentCaptor<Exception> exceptionCaptor = ArgumentCaptor.forClass(Exception.class);
        when(wordFrequencyAnalyzer.calculateHighestFrequency(anyString()))
                .thenThrow(new ConstraintViolationException(INVALID_TEXT_ERROR, Set.of()));

        //Act
        mockMvc.perform(post("/frequency/highest").content(" "))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400));

        //Assert
        verify(exceptionHandler).handleException(exceptionCaptor.capture());
        assertThat(exceptionCaptor.getValue()).isInstanceOf(ConstraintViolationException.class);
        assertThat(exceptionCaptor.getValue().getMessage()).isEqualTo(INVALID_TEXT_ERROR);
    }

    @Test
    @DisplayName("POST /frequency/highest - Failure - No request body")
    void calculateHighestFrequencyInvalidTest2() throws Exception {
        //Act and Assert
        mockMvc.perform(post("/frequency/highest"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.data").value(GENERIC_ERROR_MESSAGE));
    }

    @Test
    @DisplayName("POST /frequency/by-word/{word} - Success")
    void calculateFrequencyForWord() throws Exception {
        //Arrange
        final int frequency = RandomGenerator.getDefault().nextInt();
        when(wordFrequencyAnalyzer.calculateFrequencyForWord(anyString(), anyString()))
                .thenReturn(frequency);

        //Act and Assert
        mockMvc.perform(post("/frequency/by-word/hello")
                        .content("hello world hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data").value(frequency));
    }

    @Test
    @DisplayName("POST /frequency/by-word/{word} - Failure")
    void calculateFrequencyForWordInvalid() throws Exception {
        //Arrange
        final ArgumentCaptor<Exception> exceptionCaptor = ArgumentCaptor.forClass(Exception.class);
        when(wordFrequencyAnalyzer.calculateFrequencyForWord(anyString(), anyString()))
                .thenThrow(new ConstraintViolationException(INVALID_WORD_ERROR, Set.of()));

        //Act and Assert
        mockMvc.perform(post("/frequency/by-word/hello")
                        .content("hello world hello"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400));

        //Assert
        verify(exceptionHandler).handleException(exceptionCaptor.capture());
        assertThat(exceptionCaptor.getValue()).isInstanceOf(ConstraintViolationException.class);
        assertThat(exceptionCaptor.getValue().getMessage()).isEqualTo(INVALID_WORD_ERROR);
    }

    @Test
    @DisplayName("POST /frequency/by-limit/{limit} - Success")
    void calculateMostFrequentNWords() throws Exception {
        //Arrange
        when(wordFrequencyAnalyzer.calculateMostFrequentNWords(anyString(), anyInt()))
                .thenReturn(List.of(
                        new DefaultWordFrequency("hello", 3),
                        new DefaultWordFrequency("world", 2)
                ));

        //Act and Assert
        mockMvc.perform(post("/frequency/by-limit/2")
                        .content("hello world hello world hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].word").value("hello"))
                .andExpect(jsonPath("$.data[0].frequency").value(3))
                .andExpect(jsonPath("$.data[1].word").value("world"))
                .andExpect(jsonPath("$.data[1].frequency").value(2));
    }

    @Test
    @DisplayName("POST /frequency/by-limit/{limit} - Failure")
    void calculateMostFrequentNWordsInvalid() throws Exception {
        //Arrange
        final ArgumentCaptor<Exception> exceptionCaptor = ArgumentCaptor.forClass(Exception.class);
        when(wordFrequencyAnalyzer.calculateMostFrequentNWords(anyString(), anyInt()))
                .thenThrow(new ConstraintViolationException(INVALID_WORD_ERROR, Set.of()));

        //Act and Assert
        mockMvc.perform(post("/frequency/by-limit/2")
                        .content("hello world hello world hello"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400));

        //Assert
        verify(exceptionHandler).handleException(exceptionCaptor.capture());
        assertThat(exceptionCaptor.getValue()).isInstanceOf(ConstraintViolationException.class);
        assertThat(exceptionCaptor.getValue().getMessage()).isEqualTo(INVALID_WORD_ERROR);
    }
}
