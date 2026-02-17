package com.anva.wordfrequencyanalyzer.api;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.anva.wordfrequencyanalyzer.api.ApiResponse.failure;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    public static final String GENERIC_ERROR_MESSAGE = "Something went wrong. Contact support.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        return switch (ex) {
            case ConstraintViolationException violation -> {
                List<String> violations = violation.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                yield ResponseEntity.badRequest().body(failure(BAD_REQUEST.value(), violations));
            }
            case IllegalArgumentException illegalArgumentException -> {
                yield ResponseEntity.badRequest().body(failure(BAD_REQUEST.value(), illegalArgumentException.getLocalizedMessage()));
            }
            case NoResourceFoundException noResourceFoundException -> {
                yield ResponseEntity.badRequest().body(failure(NOT_FOUND.value(), noResourceFoundException.getLocalizedMessage()));
            }
            default -> {
                log.error(ex.getMessage(), ex);
                yield ResponseEntity.internalServerError().body(failure(INTERNAL_SERVER_ERROR.value(), GENERIC_ERROR_MESSAGE));
            }
        };
    }
}
