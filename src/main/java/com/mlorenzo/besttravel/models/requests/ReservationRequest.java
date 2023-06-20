package com.mlorenzo.besttravel.models.requests;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReservationRequest {

    @NotBlank(message = "The customer id is required")
    @Size(min = 18, max = 20, message = "The size of the customer id must be between 18 and 20 characters")
    private final String customerId;

    @NotNull(message = "The hotel id is required")
    @Positive(message = "The hotel id must be a positive number")
    private final Long hotelId;

    @NotNull(message = "The number of days is required")
    @Min(value = 1, message = "Minimum 1 day to make the reservation")
    @Max(value = 30, message = "Maximum 30 days to make the reservation")
    private final Integer totalDays;

    @NotEmpty(message = "The reservation email is required")
    @Email(message = "The email format is invalid")
    private final String email;
}
