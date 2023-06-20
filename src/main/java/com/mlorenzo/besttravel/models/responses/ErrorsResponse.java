package com.mlorenzo.besttravel.models.responses;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ErrorsResponse extends BaseErrorResponse {
    private final List<String> messages;
}
