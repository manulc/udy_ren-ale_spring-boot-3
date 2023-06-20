package com.mlorenzo.besttravel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@EqualsAndHashCode(exclude = { "reservations", "tickets", "tours" })
@ToString(exclude = { "reservations", "tickets", "tours" })
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    private String dni;

    private String fullName;
    private String creditCard;
    private String phoneNumber;
    private Integer totalFlights;
    private Integer totalLodgings;
    private Integer totalTours;

    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "customer")
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "customer")
    private Set<Tour> tours;
}
