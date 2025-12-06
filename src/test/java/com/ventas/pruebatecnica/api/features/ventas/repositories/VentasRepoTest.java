package com.ventas.pruebatecnica.api.features.ventas.repositories;

import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import com.ventas.pruebatecnica.api.features.ventas.entities.Factura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VentasRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FacturaRepository facturaRepository;

    private static final String PERSONA_ID_WITH_INVOICES = "111111111111111111";
    private static final String PERSONA_ID_WITHOUT_INVOICES = "222222222222222222";

    @BeforeEach
    void setUp() {
        // Persona with invoices
        Persona persona1 = new Persona();
        persona1.setNombre("Customer");
        persona1.setApellidoPaterno("One");
        persona1.setIdentificacion(PERSONA_ID_WITH_INVOICES);
        entityManager.persist(persona1);

        Factura factura1 = new Factura();
        factura1.setPersona(persona1);
        factura1.setFecha(LocalDate.now());
        factura1.setMonto(100.0);
        entityManager.persist(factura1);

        Factura factura2 = new Factura();
        factura2.setPersona(persona1);
        factura2.setFecha(LocalDate.now());
        factura2.setMonto(200.0);
        entityManager.persist(factura2);

        // Persona without invoices
        Persona persona2 = new Persona();
        persona2.setNombre("Customer");
        persona2.setApellidoPaterno("Two");
        persona2.setIdentificacion(PERSONA_ID_WITHOUT_INVOICES);
        entityManager.persist(persona2);

        entityManager.flush();
    }

    @Test
    void findByPersona_Identificacion_shouldReturnPagedFacturas_whenPersonaHasInvoices() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Factura> result = facturaRepository.findByPersona_Identificacion(PERSONA_ID_WITH_INVOICES, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getPersona().getIdentificacion()).isEqualTo(PERSONA_ID_WITH_INVOICES);
    }

    @Test
    void findByPersona_Identificacion_shouldReturnEmptyPage_whenPersonaHasNoInvoices() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Factura> result = facturaRepository.findByPersona_Identificacion(PERSONA_ID_WITHOUT_INVOICES, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void findByPersona_Identificacion_shouldReturnEmptyPage_whenPersonaDoesNotExist() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Factura> result = facturaRepository.findByPersona_Identificacion("non-existent-id", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();
    }
}
