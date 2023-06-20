package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.models.requests.ReservationRequest;
import com.mlorenzo.besttravel.models.responses.ReservationResponse;
import com.mlorenzo.besttravel.services.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Reservation")
@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getById(@PathVariable("reservationId") UUID id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @GetMapping("/price")
    public ResponseEntity<Map<String, BigDecimal>> getPrice(@RequestParam Long hotelId,
                                                            @RequestHeader(required = false) Currency currency) {
        if(currency == null)
            currency = Currency.getInstance("USD");
        return new ResponseEntity<>(reservationService.calculatePrice(hotelId, currency), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request) {
        return new ResponseEntity<>(reservationService.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "reservationId") UUID id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
