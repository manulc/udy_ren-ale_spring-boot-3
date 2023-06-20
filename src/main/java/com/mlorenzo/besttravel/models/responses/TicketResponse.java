package com.mlorenzo.besttravel.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TicketResponse {
    private UUID id;

    // Nota: Por defecto, un "LocalDateTime" se serializa usando el patrón "yyy-MM-ddTHH:mm:ss"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime purchaseDate;

    private BigDecimal price;
    private FlightResponse flight;
}
