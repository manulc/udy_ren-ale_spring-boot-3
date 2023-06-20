package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Set<Hotel> findByPriceLessThan(BigDecimal price);
    Set<Hotel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Set<Hotel> findByRatingGreaterThan(Integer rating);

    // Nota: Realiza un "join" normal y no un "join fetch". Por lo tanto, tendrá que hacer una consulta adicional
    // para traerse los datos de la reserva asociada al hotel cuando se invoque al método "getReservations" de ese
    // hotal.
    Optional<Hotel> findByReservationsId(UUID reservationId);
}
