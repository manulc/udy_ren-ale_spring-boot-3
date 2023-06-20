package com.mlorenzo.besttravel.services;

import com.mlorenzo.besttravel.models.responses.HotelResponse;

import java.util.Set;

public interface HotelService extends CatalogService<HotelResponse> {
    Set<HotelResponse> getGreaterThanRating(Integer rating);
}
