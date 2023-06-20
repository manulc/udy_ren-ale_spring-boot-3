package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.entities.Tour;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<Tour, Long> {
}
