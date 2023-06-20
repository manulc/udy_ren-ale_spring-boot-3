package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.annotations.Notify;
import com.mlorenzo.besttravel.config.RedisConfig;
import com.mlorenzo.besttravel.models.responses.HotelResponse;
import com.mlorenzo.besttravel.services.HotelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@Tag(name = "Hotel")
@RequiredArgsConstructor
@RestController
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;

    @Notify(value = "Hotel request")
    @GetMapping
    public Page<HotelResponse> getAll(Pageable pageable) {
        return hotelService.getAll(pageable);
    }

    // Nota: Esta anotación @Cacheable funciona tanto a nivel de controlador(este caso) como a nivel de servicio

    @Cacheable(value = RedisConfig.HOTEL_CACHE_NAME)
    @GetMapping("/less-price")
    public Set<HotelResponse> getLessPrice(@RequestParam("value") BigDecimal price) {
        return hotelService.getLessPrice(price);
    }

    @Cacheable(value = RedisConfig.HOTEL_CACHE_NAME)
    @GetMapping("/between-prices")
    public Set<HotelResponse> getBetweenPrices(@RequestParam(name = "min") BigDecimal minPrice,
                                             @RequestParam(name = "max") BigDecimal maxPrice) {
        return hotelService.getBetweenPrices(minPrice, maxPrice);
    }

    @Cacheable(value = RedisConfig.HOTEL_CACHE_NAME)
    public Set<HotelResponse> getByRating(@RequestParam(value = "value") Integer rating) {
        return hotelService.getGreaterThanRating(rating);
    }
}
