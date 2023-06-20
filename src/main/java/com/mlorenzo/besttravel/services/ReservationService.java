package com.mlorenzo.besttravel.services;

import com.mlorenzo.besttravel.models.requests.ReservationRequest;
import com.mlorenzo.besttravel.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

public interface ReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {
    Map<String, BigDecimal> calculatePrice(Long hotelId, Currency currency);
}
