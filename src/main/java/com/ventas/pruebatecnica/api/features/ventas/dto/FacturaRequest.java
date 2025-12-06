package com.ventas.pruebatecnica.api.features.ventas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FacturaRequest {
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    @NotNull(message = "La identificación del cliente es obligatoria")
    private String identificacionPersona;
}