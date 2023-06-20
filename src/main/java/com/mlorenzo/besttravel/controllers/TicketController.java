package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.models.requests.TicketRequest;
import com.mlorenzo.besttravel.models.responses.TicketResponse;
import com.mlorenzo.besttravel.services.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Ticket")
@RequiredArgsConstructor
@RestController
@RequestMapping("tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("{id}")
    public TicketResponse getById(@PathVariable UUID id) {
        return ticketService.getById(id);
    }

    @GetMapping("price")
    public Map<String, BigDecimal> getPrice(@RequestParam Long flyId,
                                            @RequestHeader(required = false) Currency currency) {
        if(currency == null)
            currency = Currency.getInstance("USD");
        return ticketService.calculatePrice(flyId, currency);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TicketResponse create(@RequestBody @Validated TicketRequest request) {
        return ticketService.create(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id) {
        ticketService.delete(id);
    }
}
