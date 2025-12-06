package com.ventas.pruebatecnica.api.features.ventas.services;

import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import com.ventas.pruebatecnica.api.features.directorio.repositories.PersonaRepository;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaRequest;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaResponse;
import com.ventas.pruebatecnica.api.features.ventas.mappers.VentasMapper;
import com.ventas.pruebatecnica.api.features.ventas.entities.Factura;
import com.ventas.pruebatecnica.api.features.ventas.repositories.FacturaRepository;
import com.ventas.pruebatecnica.api.features.ventas.services.impl.VentasServiceImpl;
import com.ventas.pruebatecnica.api.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentasImplTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private VentasMapper mapper;

    @InjectMocks
    private VentasServiceImpl ventasService;

    @Test
    void findFacturasByPersona_shouldReturnPageOfFacturas_whenPersonaExists() {
        String identificacion = "12345";
        Pageable pageable = PageRequest.of(0, 10);
        Factura factura = new Factura();
        Page<Factura> facturaPage = new PageImpl<>(Collections.singletonList(factura));
        FacturaResponse facturaResponse = new FacturaResponse();

        when(personaRepository.existsByIdentificacion(identificacion)).thenReturn(true);
        when(facturaRepository.findByPersona_Identificacion(identificacion, pageable)).thenReturn(facturaPage);
        when(mapper.toResponse(any(Factura.class))).thenReturn(facturaResponse);

        Page<FacturaResponse> result = ventasService.findFacturasByPersona(identificacion, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(personaRepository).existsByIdentificacion(identificacion);
        verify(facturaRepository).findByPersona_Identificacion(identificacion, pageable);
    }

    @Test
    void findFacturasByPersona_shouldThrowNotFoundException_whenPersonaDoesNotExist() {
        String identificacion = "non-existent";
        Pageable pageable = PageRequest.of(0, 10);
        when(personaRepository.existsByIdentificacion(identificacion)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            ventasService.findFacturasByPersona(identificacion, pageable);
        });

        verify(facturaRepository, never()).findByPersona_Identificacion(anyString(), any(Pageable.class));
    }

    @Test
    void storeFactura_shouldSaveAndReturnFactura_whenPersonaExists() {
        FacturaRequest request = new FacturaRequest();
        request.setIdentificacionPersona("12345");
        Persona persona = new Persona();
        Factura facturaToSave = new Factura();
        Factura savedFactura = new Factura();
        savedFactura.setId(1L);
        FacturaResponse expectedResponse = new FacturaResponse();
        expectedResponse.setId(1L);

        when(personaRepository.findByIdentificacion(request.getIdentificacionPersona())).thenReturn(Optional.of(persona));
        when(mapper.toEntity(request)).thenReturn(facturaToSave);
        when(facturaRepository.save(facturaToSave)).thenReturn(savedFactura);
        when(mapper.toResponse(savedFactura)).thenReturn(expectedResponse);

        FacturaResponse result = ventasService.storeFactura(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(personaRepository).findByIdentificacion(request.getIdentificacionPersona());
        verify(facturaRepository).save(facturaToSave);
    }

    @Test
    void storeFactura_shouldThrowNotFoundException_whenPersonaDoesNotExist() {
        FacturaRequest request = new FacturaRequest();
        request.setIdentificacionPersona("non-existent");

        when(personaRepository.findByIdentificacion(request.getIdentificacionPersona())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            ventasService.storeFactura(request);
        });

        verify(facturaRepository, never()).save(any(Factura.class));
    }
}
