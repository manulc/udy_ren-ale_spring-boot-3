package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("select f from Flight f where f.price < ?1")
    Set<Flight> selectLessPrice(BigDecimal price);

    @Query("select f from Flight f where f.price between :min and :max")
    Set<Flight> selectBetweenPrice(BigDecimal min, BigDecimal max);

    @Query("select f from Flight f where f.originName=:origin and f.destinyName=:destiny")
    Set<Flight> selectOriginDestiny(@Param("origin") String originName, @Param("destiny") String destinyName);

    // Nota: Sin la palabra "fetch" en la consulta, no se trae directamente los datos del ticket, es decir,
    // cuando se invoque al método "getTickets" del vuelo correspondiente, tendrá que realizar una nueva consulta
    // para traerse los datos de ese ticket asociado. Sin embargo, con el uso de la palabra "fetch", sí se trae
    // directamente los datos del ticket asociado al vuelo.
    //@Query("select f from Flight f join f.tickets t where t.id=:id")
    @Query("select f from Flight f join fetch f.tickets t where t.id=:id")
    Optional<Flight> findByTicketId(UUID id);
}
