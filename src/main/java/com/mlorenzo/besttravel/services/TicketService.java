package com.mlorenzo.besttravel.services;

import com.mlorenzo.besttravel.models.requests.TicketRequest;
import com.mlorenzo.besttravel.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

public interface TicketService extends CrudService<TicketRequest, TicketResponse, UUID> {
    Map<String, BigDecimal> calculatePrice(Long flightId, Currency currency);
}
