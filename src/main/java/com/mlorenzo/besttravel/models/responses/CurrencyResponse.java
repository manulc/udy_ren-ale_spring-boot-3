package com.mlorenzo.besttravel.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class CurrencyResponse {

    // Mapea el campo "date" del Json de la respuesta a esta propiedad
    @JsonProperty(value = "date")
    private LocalDate exchangeDate;

    private Map<String, BigDecimal> rates;
}
