package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.models.requests.TourRequest;
import com.mlorenzo.besttravel.models.responses.TourResponse;
import com.mlorenzo.besttravel.services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Tour")
@AllArgsConstructor
@RestController
@RequestMapping("tours")
public class TourController {
    private final TourService tourService;

    @Operation(summary = "This operation returns a tour from an id")
    @GetMapping("{id}")
    public TourResponse getById(@PathVariable Long id) {
        return tourService.getById(id);
    }

    @Operation(summary = " This operation saves in the system a tour based on a list of flights and reservations")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TourResponse create(@Valid @RequestBody TourRequest request) {
        return tourService.create(request);
    }

    @Operation(summary = "This operation deletes a tour from an id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        tourService.delete(id);
    }

    @Operation(summary = "This operation removes an existing ticket from a tour")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{tourId}/delete-ticket/{ticketId}")
    public void deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        tourService.deleteTicket(tourId, ticketId);
    }

    @Operation(summary = "This operation creates and adds a ticket to a tour")
    @PatchMapping("{tourId}/add-ticket/{flightId}")
    public Map<String, UUID> addTicket(@PathVariable Long tourId, @PathVariable Long flightId) {
        return Map.of("ticketId", tourService.addTicket(tourId, flightId));
    }

    @Operation(summary = "This operation removes an existing reservation from a tour")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{tourId}/delete-reservation/{reservationId}")
    public void deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
        tourService.deleteReservation(tourId, reservationId);
    }

    @Operation(summary = "This operation creates and adds a reservation to a tour")
    @PatchMapping("{tourId}/add-reservation/{hotelId}")
    public Map<String, UUID> addReservation(@PathVariable Long tourId, @PathVariable Long hotelId,
                       @RequestParam Integer totalDays) {
        return Map.of("reservationId", tourService.addReservation(tourId, hotelId, totalDays));
    }
}
