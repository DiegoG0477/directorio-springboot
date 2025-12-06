package com.ventas.pruebatecnica.api.features.directorio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonaRequest {
    @NotBlank(message = "First name is mandatory")
    private String nombre;

    @NotBlank(message = "Last name is mandatory")
    private String apellidoPaterno;

    private String apellidoMaterno;

    @NotBlank(message = "Identification is mandatory")
    @Size(min = 18, max = 18, message = "Identification must be exactly 18 characters long")
    private String identificacion;
}
