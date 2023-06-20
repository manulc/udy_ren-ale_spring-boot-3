package com.mlorenzo.besttravel.models.responses;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ErrorResponse extends BaseErrorResponse {
    private final String message;
}
