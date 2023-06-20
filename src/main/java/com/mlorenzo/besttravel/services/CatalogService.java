package com.mlorenzo.besttravel.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

public interface CatalogService<R> {
    Page<R> getAll(Pageable pageable);
    Set<R> getLessPrice(BigDecimal price);
    Set<R> getBetweenPrices(BigDecimal minPrice, BigDecimal maxPrice);
}
