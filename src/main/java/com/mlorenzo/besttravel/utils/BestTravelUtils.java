package com.mlorenzo.besttravel.utils;

import com.mlorenzo.besttravel.clients.CurrencyClient;
import com.mlorenzo.besttravel.domain.entities.*;
import com.mlorenzo.besttravel.models.responses.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BestTravelUtils {
    public static final BigDecimal RESERVATION_CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.20);
    public static final BigDecimal TICKET_CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);
    private static final Random random = new Random();

    private final CurrencyClient currencyClient;

    public static LocalDateTime generateRandomDateTime(final int minHours, final int maxHours) {
        // Quitamos los nanosegundos porque no queremos guardarlos en la base de datos
        final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        final int randomHours = random.nextInt(maxHours) + minHours;
        return now.plusHours(randomHours);
    }

    private static LocalDate generateRandomDate(final int minDays, final int maxDays) {
        final int randomDays = random.nextInt(maxDays) + minDays;
        return LocalDate.now().plusDays(randomDays);
    }

    public BigDecimal calculatePrice(final BigDecimal basePrice,
                                            final BigDecimal charger_price_percentage,
                                            final Currency currency) {
        final BigDecimal usdPrice = basePrice.add(basePrice.multiply(charger_price_percentage))
                .setScale(2, RoundingMode.UP);
        if(currency.getCurrencyCode().equals("USD"))
            return usdPrice;
        final CurrencyResponse currencyResponse = currencyClient.getCurrency(currency);
        return usdPrice.multiply(currencyResponse.getRates().get(currency.getCurrencyCode()))
                .setScale(2, RoundingMode.UP);
    }

    public Reservation createReservation(final Customer customer, final Hotel hotel, final int totalDays,
                                         final Tour tour) {
        final LocalDate startDate = BestTravelUtils.generateRandomDate(1, 5);
        final BigDecimal price = calculatePrice(hotel.getPrice(), RESERVATION_CHARGER_PRICE_PERCENTAGE,
                Currency.getInstance("USD")).multiply(BigDecimal.valueOf(totalDays));
        // Quitamos los nanosegundos porque no queremos guardarlos en la base de datos
        final LocalDateTime now = LocalDateTime.now().withNano(0);
        return Reservation.builder()
                .id(UUID.randomUUID())
                .dateTimeReservation(now)
                .dateStart(startDate)
                .dateEnd(startDate.plusDays(totalDays))
                .totalDays(totalDays)
                .price(price)
                .customer(customer)
                .hotel(hotel)
                .tour(tour)
                .build();
    }

    public Ticket createTicket(final Customer customer, final Flight flight, final Tour tour) {
        return Ticket.builder()
                .id(UUID.randomUUID())
                .price(calculatePrice(flight.getPrice(), TICKET_CHARGER_PRICE_PERCENTAGE,
                        Currency.getInstance("USD")))
                .arrivalDate(BestTravelUtils.generateRandomDateTime(6, 12))
                .departureDate(BestTravelUtils.generateRandomDateTime(1, 5))
                // Quitamos los nanosegundos porque no queremos guardarlos en la base de datos
                .purchaseDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .customer(customer)
                .flight(flight)
                .tour(tour)
                .build();
    }
}
