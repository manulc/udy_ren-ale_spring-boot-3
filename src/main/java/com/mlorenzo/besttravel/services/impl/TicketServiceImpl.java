package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.clients.EmailClient;
import com.mlorenzo.besttravel.domain.entities.Customer;
import com.mlorenzo.besttravel.domain.entities.Flight;
import com.mlorenzo.besttravel.domain.entities.Ticket;
import com.mlorenzo.besttravel.exceptions.NotFoundException;
import com.mlorenzo.besttravel.models.requests.TicketRequest;
import com.mlorenzo.besttravel.models.responses.FlightResponse;
import com.mlorenzo.besttravel.models.responses.TicketResponse;
import com.mlorenzo.besttravel.repositories.CustomerRepository;
import com.mlorenzo.besttravel.repositories.FlightRepository;
import com.mlorenzo.besttravel.repositories.TicketRepository;
import com.mlorenzo.besttravel.services.TicketService;
import com.mlorenzo.besttravel.utils.BestTravelUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {
    private final FlightRepository flightRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final BestTravelUtils bestTravelUtils;
    private final EmailClient emailClient;

    @Override
    public TicketResponse getById(UUID id) {
        return ticketRepository.findById(id)
                // Versión simplificada de la expresión "ticket -> entityToResponse(ticket)"
                .map(this::entityToResponse)
                .orElseThrow(() -> new NotFoundException(String.format("The ticket with id %s does not exists", id)));
    }

    @Override
    public TicketResponse create(TicketRequest request) {
        final Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("The flight with id %d does not exists", request.getFlightId())));
        final Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("The customer with id %s does not exists", request.getCustomerId())));
        final Ticket savedTicket = ticketRepository.save(bestTravelUtils.createTicket(customer, flight, null));
        emailClient.sendMail(request.getEmail(), customer.getFullName(), "Ticket");
        return entityToResponse(savedTicket);
    }

    @Override
    public void delete(UUID id) {
        if(ticketRepository.existsById(id))
            ticketRepository.deleteById(id);
        else
            throw new NotFoundException(String.format("The ticket with id %s does not exists", id));
    }

    @Override
    public Map<String, BigDecimal> calculatePrice(Long flightId, Currency currency) {
        return flightRepository.findById(flightId)
                .map(flight -> {
                    final BigDecimal price = bestTravelUtils.calculatePrice(flight.getPrice(),
                            BestTravelUtils.TICKET_CHARGER_PRICE_PERCENTAGE, currency);
                    return Map.of("price", price);
                })
                .orElseThrow(() -> new NotFoundException(String
                        .format("The flight with id %d does not exists", flightId)));
    }

    private TicketResponse entityToResponse(final Ticket ticket) {
        final TicketResponse ticketResponse = new TicketResponse();
        final FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(ticket, ticketResponse);
        BeanUtils.copyProperties(ticket.getFlight(), flightResponse);
        ticketResponse.setFlight(flightResponse);
        return ticketResponse;
    }
}
