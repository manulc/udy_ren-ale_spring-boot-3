package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.exceptions.NotFoundException;
import com.mlorenzo.besttravel.models.responses.ErrorResponse;
import com.mlorenzo.besttravel.models.responses.ErrorsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorHandlerController {

    // Nota: Estos métodos pueden ser públicos o privados

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    private ErrorResponse handleNotFoundException(final NotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ErrorsResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final List<String> messages = ex.getAllErrors().stream()
                // Versión simplificada de la expresión "error -> error.getDefaultMessage()"
                .map(ObjectError::getDefaultMessage)
                .toList();
        return ErrorsResponse.builder()
                .messages(messages)
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
    }
}
