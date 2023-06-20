package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.clients.EmailClient;
import com.mlorenzo.besttravel.domain.entities.*;
import com.mlorenzo.besttravel.exceptions.NotFoundException;
import com.mlorenzo.besttravel.models.requests.TourHotelRequest;
import com.mlorenzo.besttravel.models.requests.TourRequest;
import com.mlorenzo.besttravel.models.responses.TourResponse;
import com.mlorenzo.besttravel.repositories.*;
import com.mlorenzo.besttravel.services.TourService;
import com.mlorenzo.besttravel.utils.BestTravelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final FlightRepository flightRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final BestTravelUtils bestTravelUtils;
    private final EmailClient emailClient;

    @Override
    public TourResponse getById(Long id) {
        return tourRepository.findById(id)
                // Versión simplificada de la expresión "tour -> entityToResponse(tour)"
                .map(this::entityToResponse)
                .orElseThrow(() -> new NotFoundException(String.format("Tour with id %d not found", id)));
    }

    @Transactional
    @Override
    public TourResponse create(TourRequest request) {
        final Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("Customer with id %s not found", request.getCustomerId())));
        final List<Flight> flights = flightRepository.findAllById(request.getFlightIds());
        final Tour savedTour = tourRepository.save(Tour.builder().customer(customer).build());
        final List<Ticket> tickets = flights.stream()
                .map(flight -> bestTravelUtils.createTicket(customer, flight, savedTour))
                .toList();
        final List<Ticket> savedTickets = (List<Ticket>) ticketRepository.saveAll(tickets);
        final List<Long> hotelIds = request.getHotels().stream()
                // Versión simplificada de la expresión "thr -> thr.getId()"
                .map(TourHotelRequest::getId)
                .toList();
        final List<Hotel> hotels = hotelRepository.findAllById(hotelIds);
        final List<Reservation> reservations = IntStream.iterate(0, i -> i < hotels.size(), i -> i + 1)
                .mapToObj(i -> bestTravelUtils
                        .createReservation(customer, hotels.get(i), request.getHotels().get(i).getTotalDays(), savedTour))
                .toList();
        final List<Reservation> savedReservations = (List<Reservation>) reservationRepository.saveAll(reservations);
        savedTour.setTickets(savedTickets);
        savedTour.setReservations(savedReservations);
        emailClient.sendMail(request.getEmail(), customer.getFullName(), "Tour");
        return entityToResponse(savedTour);
    }

    @Override
    public void delete(Long id) {
        if(tourRepository.existsById(id))
            tourRepository.deleteById(id);
        else
            throw new NotFoundException(String.format("Tour with id %d not found", id));
    }

    @Override
    public void deleteTicket(Long tourId, UUID ticketId) {
        if(!tourRepository.existsById(tourId))
            throw new NotFoundException(String.format("Tour with id %d not found", tourId));
        final int num_deletes = ticketRepository.deleteByIdAndTourId(ticketId, tourId);
        if(num_deletes != 1)
            throw new NotFoundException(String.format("Ticket with id %s not found", ticketId));

    }

    @Override
    public UUID addTicket(Long tourId, Long flightId) {
        final Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException(String.format("Tour with id %d not found", tourId)));
        final Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException(String.format("Flight with id %d not found", flightId)));
        final Ticket ticket = bestTravelUtils.createTicket(tour.getCustomer(), flight, tour);
        return ticketRepository.save(ticket).getId();
    }

    @Override
    public void deleteReservation(Long tourId, UUID reservationId) {
        if(!tourRepository.existsById(tourId))
            throw new NotFoundException(String.format("Tour with id %d not found", tourId));
        final int num_deletes = reservationRepository.deleteByIdAndTourId(reservationId, tourId);
        if(num_deletes != 1)
            throw new NotFoundException(String.format("Reservation with id %s not found", reservationId));
    }

    @Override
    public UUID addReservation(Long tourId, Long hotelId, Integer totalDays) {
        final Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException(String.format("Tour with id %d not found", tourId)));
        final Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException(String.format("Hotel with id %d not found", hotelId)));
        final Reservation reservation = bestTravelUtils.createReservation(tour.getCustomer(), hotel, totalDays, tour);
        return reservationRepository.save(reservation).getId();
    }

    private TourResponse entityToResponse(final Tour tour) {
        final TourResponse tourResponse = new TourResponse();
        tourResponse.setId(tour.getId());
        // Versión simplificada de la expresión "ticket -> ticket.getId()"
        tourResponse.setTicketIds(tour.getTickets().stream().map(Ticket::getId).toList());
        // Versión simplificada de la expresión "reservation -> reservation.getId()"
        tourResponse.setReservationIds(tour.getReservations().stream().map(Reservation::getId).toList());
        return tourResponse;
    }
}
