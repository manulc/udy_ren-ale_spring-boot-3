package com.mlorenzo.besttravel.domain.entities;

import com.mlorenzo.besttravel.utils.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "numeric")
    private Float originLat;

    @Column(columnDefinition = "numeric")
    private Float originLng;

    @Column(columnDefinition = "numeric")
    private Float destinyLat;

    @Column(columnDefinition = "numeric")
    private Float destinyLng;

    private String originName;
    private String destinyName;
    private BigDecimal price;


    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "flight")
    private Set<Ticket> tickets;
}
