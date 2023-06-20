package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.domain.entities.Hotel;
import com.mlorenzo.besttravel.models.responses.HotelResponse;
import com.mlorenzo.besttravel.repositories.HotelRepository;
import com.mlorenzo.besttravel.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Page<HotelResponse> getAll(Pageable pageable) {
        // Versión simplificada de la expresión "hotel -> entityToResponse(hotel)"
        return hotelRepository.findAll(pageable).map(this::entityToResponse);
    }

    @Override
    public Set<HotelResponse> getLessPrice(BigDecimal price) {
        // Simulamos un retraso de 4 seg para probar la caché Redis
        try {
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return hotelRepository.findByPriceLessThan(price).stream()
                // Versión simplificada de la expresión "hotel -> entityToResponse(hotel)"
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> getBetweenPrices(BigDecimal minPrice, BigDecimal maxPrice) {
        // Simulamos un retraso de 4 seg para probar la caché Redis
        try {
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return hotelRepository.findByPriceBetween(minPrice, maxPrice).stream()
                // Versión simplificada de la expresión "hotel -> entityToResponse(hotel)"
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Cacheable(value = "hotel")
    @Override
    public Set<HotelResponse> getGreaterThanRating(Integer rating) {
        // Simulamos un retraso de 4 seg para probar la caché Redis
        try {
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return hotelRepository.findByRatingGreaterThan(rating).stream()
                // Versión simplificada de la expresión "hotel -> entityToResponse(hotel)"
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    private HotelResponse entityToResponse(final Hotel hotel) {
        final HotelResponse hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(hotel, hotelResponse);
        return hotelResponse;
    }
}
