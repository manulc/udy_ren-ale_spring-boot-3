package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.config.RedisConfig;
import com.mlorenzo.besttravel.domain.entities.Flight;
import com.mlorenzo.besttravel.models.responses.FlightResponse;
import com.mlorenzo.besttravel.repositories.FlightRepository;
import com.mlorenzo.besttravel.services.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Override
    public Page<FlightResponse> getAll(Pageable pageable) {
        // Versión simplificada de la expresión "flight -> entityToResponse(flight)"
        return flightRepository.findAll(pageable).map(this::entityToResponse);
    }

    @Cacheable(value = RedisConfig.FLIGHT_CACHE_NAME)
    @Override
    public Set<FlightResponse> getLessPrice(BigDecimal price) {
        // Simulamos un retraso de 4 seg para probar la caché Redis
        try {
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return flightRepository.selectLessPrice(price).stream()
                // Versión simplificada de la expresión "flight -> entityToResponse(flight)"
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Nota: Esta anotación @Cacheable funciona tanto a nivel de controlador como a nivel de servicio(este caso)

    @Cacheable(value = RedisConfig.FLIGHT_CACHE_NAME)
    @Override
    public Set<FlightResponse> getBetweenPrices(BigDecimal minPrice, BigDecimal maxPrice) {
        // Simulamos un retraso de 4 seg para probar la caché Redis
        try {
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return flightRepository.selectBetweenPrice(minPrice, maxPrice).stream()
                // Versión simplificada de la expresión "flight -> entityToResponse(flight)"
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Cacheable(value = RedisConfig.FLIGHT_CACHE_NAME)
    @Override
    public Set<FlightResponse> getByOriginDestiny(String originName, String destinyName) {
        // Simulamos un retraso de 4 seg para probar la caché Redis
        try {
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return flightRepository.selectOriginDestiny(originName, destinyName).stream()
                // Versión simplificada de la expresión "flight -> entityToResponse(flight)"
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    private FlightResponse entityToResponse(final Flight fly) {
        final FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(fly, flightResponse);
        return flightResponse;
    }
}
