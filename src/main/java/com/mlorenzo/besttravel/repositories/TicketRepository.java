package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.entities.Ticket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<Ticket, UUID> {

    @Transactional
    @Modifying
    @Query("delete from Ticket t where t.id=?1 and t.tour.id=?2")
    int deleteByIdAndTourId(UUID ticketId, Long tourId);
}
