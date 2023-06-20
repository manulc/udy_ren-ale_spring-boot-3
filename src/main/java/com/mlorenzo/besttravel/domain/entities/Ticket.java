package com.mlorenzo.besttravel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    private UUID id;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private LocalDateTime purchaseDate;
    private BigDecimal price;

    @ToString.Exclude
    @ManyToOne
    private Flight flight;

    @ManyToOne
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @PrePersist
    void incrementCustomerFlights() {
        customer.setTotalFlights(customer.getTotalFlights() + 1);
    }
}
