package com.mlorenzo.besttravel.models.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class TourRequest {

    @NotBlank(message = "The customer id is required")
    @Size(min = 18, max = 20, message = "The size of the customer id must be between 18 and 20 characters")
    private final String customerId;

    @Size(min = 1, message = "There must be at least one flight id")
    private final List<@Positive(message = "The flight id must be a positive number") Long> flightIds;

    @Valid
    @Size(min = 1, message = "There must be at least one reservation")
    private final List<TourHotelRequest> hotels;

    @NotEmpty(message = "The tour email is required")
    @Email(message = "The email format is invalid")
    private final String email;
}
