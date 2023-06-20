package com.mlorenzo.besttravel.models.responses;

import com.mlorenzo.besttravel.utils.AeroLine;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

// Nota: En este caso, la implementazión de la interfaz Serializable es requerida por la caché Redis para porder
// almacenar los estados de los objetos de esta clase

@Getter
@Setter
public class FlightResponse implements Serializable {
    private Long id;
    private Float originLat;
    private Float originLng;
    private Float destinyLat;
    private Float destinyLng;
    private String originName;
    private String destinyName;
    private BigDecimal price;
    private AeroLine aeroLine;
}
