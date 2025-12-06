package com.ventas.pruebatecnica.api.features.ventas.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FacturaResponse {
    private Long id;
    private LocalDate fecha;
    private Double monto;
}