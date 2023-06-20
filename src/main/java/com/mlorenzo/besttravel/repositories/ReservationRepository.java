package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.entities.Reservation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {

    @Transactional
    @Modifying
    @Query("delete from Reservation r where r.id=:reservationId and r.tour.id=:tourId")
    int deleteByIdAndTourId(UUID reservationId, Long tourId);
}
