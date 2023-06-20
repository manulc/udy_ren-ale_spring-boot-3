package com.mlorenzo.besttravel.services;

import com.mlorenzo.besttravel.models.requests.TourRequest;
import com.mlorenzo.besttravel.models.responses.TourResponse;

import java.util.UUID;

public interface TourService extends CrudService<TourRequest, TourResponse, Long> {
    void deleteTicket(Long tourId, UUID ticketId);
    UUID addTicket(Long tourId, Long flightId);
    void deleteReservation(Long tourId, UUID reservationId);
    UUID addReservation(Long tourId, Long hotelId, Integer totalDays);
}
