package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.clients.EmailClient;
import com.mlorenzo.besttravel.domain.entities.Customer;
import com.mlorenzo.besttravel.domain.entities.Hotel;
import com.mlorenzo.besttravel.domain.entities.Reservation;
import com.mlorenzo.besttravel.exceptions.NotFoundException;
import com.mlorenzo.besttravel.models.requests.ReservationRequest;
import com.mlorenzo.besttravel.models.responses.HotelResponse;
import com.mlorenzo.besttravel.models.responses.ReservationResponse;
import com.mlorenzo.besttravel.repositories.CustomerRepository;
import com.mlorenzo.besttravel.repositories.HotelRepository;
import com.mlorenzo.besttravel.repositories.ReservationRepository;
import com.mlorenzo.besttravel.services.ReservationService;
import com.mlorenzo.besttravel.utils.BestTravelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final BestTravelUtils bestTravelUtils;
    private final EmailClient emailClient;

    @Override
    public ReservationResponse getById(UUID id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if(optionalReservation.isPresent())
            return entityToResponse(optionalReservation.get());
        throw new NotFoundException(String.format("Reservation with id %s not found ", id));
    }

    @Override
    public ReservationResponse create(ReservationRequest request) {
        final Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("Hotel with id %d not found ", request.getHotelId())));
        final Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NotFoundException(String
                        .format("Customer with id %s not found ", request.getCustomerId())));
        final Reservation savedReservation = reservationRepository
                .save(bestTravelUtils.createReservation(customer, hotel, request.getTotalDays(), null));
        emailClient.sendMail(request.getEmail(), customer.getFullName(), "Reservation");
        return entityToResponse(savedReservation);
    }

    @Override
    public void delete(UUID id) {
        if(!reservationRepository.existsById(id))
            throw new NotFoundException(String.format("Reservation with id %s not found", id));
        reservationRepository.deleteById(id);
    }

    @Override
    public Map<String, BigDecimal> calculatePrice(Long hotelId, Currency currency) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if(optionalHotel.isPresent()) {
            final BigDecimal price = bestTravelUtils.calculatePrice(optionalHotel.get().getPrice(),
                    BestTravelUtils.RESERVATION_CHARGER_PRICE_PERCENTAGE, currency);
            return Collections.singletonMap("price", price);
        }
        throw new NotFoundException(String.format("Hotel with id %d not found", hotelId));
    }

    private ReservationResponse entityToResponse(final Reservation reservation) {
        final ReservationResponse reservationResponse = new ReservationResponse();
        final HotelResponse hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(reservation, reservationResponse);
        BeanUtils.copyProperties(reservation.getHotel(), hotelResponse);
        reservationResponse.setHotel(hotelResponse);
        return reservationResponse;
    }
}
