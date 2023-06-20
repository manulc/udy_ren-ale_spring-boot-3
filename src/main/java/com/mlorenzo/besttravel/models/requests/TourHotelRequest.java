package com.mlorenzo.besttravel.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TourHotelRequest {

    @NotNull(message = "The hotel id is required")
    @Positive(message = "The hotel id must be a positive number")
    private final Long id;

    @NotNull(message = "The number of days is required")
    @Min(value = 1, message = "Minimum 1 day to make the reservation")
    @Max(value = 30, message = "Maximum 30 days to make the reservation")
    private final Integer totalDays;
}
