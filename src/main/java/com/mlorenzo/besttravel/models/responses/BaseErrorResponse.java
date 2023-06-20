package com.mlorenzo.besttravel.models.responses;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BaseErrorResponse {
    private final Integer code;
    private final String status;
}
