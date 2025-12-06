package com.ventas.pruebatecnica.api.features.directorio.dto;

import lombok.Data;

@Data
public class PersonaResponse {
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String identificacion;
}