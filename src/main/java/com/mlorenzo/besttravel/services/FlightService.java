package com.mlorenzo.besttravel.services;

import com.mlorenzo.besttravel.models.responses.FlightResponse;

import java.util.Set;

public interface FlightService extends CatalogService<FlightResponse> {
    Set<FlightResponse> getByOriginDestiny(String originName, String destinyName);
}
