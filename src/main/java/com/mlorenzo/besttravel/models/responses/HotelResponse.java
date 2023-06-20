package com.mlorenzo.besttravel.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

// Nota: En este caso, la implementazión de la interfaz Serializable es requerida por la caché Redis para porder
// almacenar los estados de los objetos de esta clase

@Getter
@Setter
public class HotelResponse implements Serializable {
    private Long id;
    private String name;
    private String address;
    private Integer rating;
    private BigDecimal price;
}
