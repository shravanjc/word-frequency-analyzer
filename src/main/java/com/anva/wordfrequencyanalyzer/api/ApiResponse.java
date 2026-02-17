package com.anva.wordfrequencyanalyzer.api;

import static org.springframework.http.HttpStatus.OK;

public record ApiResponse<T>(int statusCode, T data) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OK.value(), data);
    }

    public static <T> ApiResponse<T> failure(int statusCode, T data) {
        return new ApiResponse<>(statusCode, data);
    }
}
