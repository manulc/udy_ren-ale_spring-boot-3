package com.mlorenzo.besttravel.models.requests;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TicketRequest {

    @NotBlank(message = "The customer id is required")
    @Size(min = 18, max = 20, message = "The size of the customer id must be between 18 and 20 characters")
    private final String customerId;

    @NotNull(message = "The flight id is required")
    @Positive(message = "The flight id must be a positive number")
    private final Long flightId;

    @NotEmpty(message = "The ticket email is required")
    @Email(message = "The email format is invalid")
    private final String email;
}
