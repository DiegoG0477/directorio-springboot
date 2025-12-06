package com.ventas.pruebatecnica.api.features.directorio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String apellidoPaterno;

    private String apellidoMaterno;

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;
}