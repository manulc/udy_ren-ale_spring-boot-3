package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.annotations.Notify;
import com.mlorenzo.besttravel.models.responses.FlightResponse;
import com.mlorenzo.besttravel.services.FlightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@Tag(name = "Flight")
@AllArgsConstructor
@RestController
@RequestMapping("/flights")
public class FlightController {
    private  final FlightService flightService;

    @Notify
    @GetMapping
    public Page<FlightResponse> getAll(Pageable pageable) {
        return flightService.getAll(pageable);
    }

    @GetMapping("/less-price")
    public Set<FlightResponse> getLessPrice(@RequestParam("value") BigDecimal price) {
        return flightService.getLessPrice(price);
    }

    @GetMapping("/between-prices")
    public Set<FlightResponse> getBetweenPrices(@RequestParam(value = "min") BigDecimal minPrice,
                                                @RequestParam(value = "max") BigDecimal maxPrice) {
        return flightService.getBetweenPrices(minPrice, maxPrice);
    }

    @GetMapping("/origin-destiny")
    public Set<FlightResponse> getOriginDestiny(@RequestParam(name = "origin") String originName,
                                                @RequestParam(name = "destiny") String destinyName) {
        return flightService.getByOriginDestiny(originName, destinyName);
    }
}
