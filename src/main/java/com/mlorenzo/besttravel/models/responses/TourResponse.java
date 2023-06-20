package com.mlorenzo.besttravel.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TourResponse {
    private Long id;
    private List<UUID> ticketIds;
    private  List<UUID> reservationIds;
}
