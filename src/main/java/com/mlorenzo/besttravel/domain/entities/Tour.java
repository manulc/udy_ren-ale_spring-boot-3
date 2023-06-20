package com.mlorenzo.besttravel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "reservations", "tickets" })
@ToString(exclude = { "reservations", "tickets" })
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tour")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "tour")
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @PrePersist
    void incrementCustomerTours() {
        customer.setTotalTours(customer.getTotalTours() + 1);
    }
}
