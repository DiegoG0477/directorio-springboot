package com.ventas.pruebatecnica.api.features.directorio.entities;

import com.ventas.pruebatecnica.api.features.ventas.entities.Factura;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personas")
@Getter
@Setter
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String nombre;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String apellidoPaterno;

    private String apellidoMaterno;

    @NotBlank
    @Column(nullable = false, length = 18, unique = true)
    private String identificacion;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas = new ArrayList<>();
}
