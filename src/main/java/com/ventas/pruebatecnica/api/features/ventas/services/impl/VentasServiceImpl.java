package com.ventas.pruebatecnica.api.features.ventas.services.impl;

import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import com.ventas.pruebatecnica.api.features.directorio.repositories.PersonaRepository;
import com.ventas.pruebatecnica.api.features.ventas.mappers.VentasMapper;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaRequest;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaResponse;
import com.ventas.pruebatecnica.api.features.ventas.entities.Factura;
import com.ventas.pruebatecnica.api.features.ventas.repositories.FacturaRepository;
import com.ventas.pruebatecnica.api.features.ventas.services.VentasService;
import com.ventas.pruebatecnica.api.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VentasServiceImpl implements VentasService {

    private final FacturaRepository facturaRepository;
    private final PersonaRepository personaRepository;
    private final VentasMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaResponse> findFacturasByPersona(String identificacion, Pageable pageable) {
        log.info("Fetching invoices for customer with identification: {}", identificacion);

        if (!personaRepository.existsByIdentificacion(identificacion)) {
            log.warn("Attempted to fetch invoices for a non-existent customer: {}", identificacion);
            throw new ResourceNotFoundException("Customer not found with identification " + identificacion);
        }

        Page<FacturaResponse> facturas = facturaRepository.findByPersona_Identificacion(identificacion, pageable)
                .map(mapper::toResponse);
        
        log.info("Found {} invoices for customer with identification: {}", facturas.getTotalElements(), identificacion);
        return facturas;
    }

    @Override
    @Transactional
    public FacturaResponse storeFactura(FacturaRequest request) {
        log.info("Processing new invoice for customer: {}", request.getIdentificacionPersona());

        Persona persona = personaRepository.findByIdentificacion(request.getIdentificacionPersona())
                .orElseThrow(() -> {
                    log.error("Invoice creation failed. Customer not found: {}", request.getIdentificacionPersona());
                    return new ResourceNotFoundException("Cannot create invoice: Customer not found with identification " + request.getIdentificacionPersona());
                });

        Factura factura = mapper.toEntity(request);
        factura.setPersona(persona);

        Factura savedFactura = facturaRepository.save(factura);
        log.info("Successfully created invoice with ID: {} for customer: {}", savedFactura.getId(), persona.getIdentificacion());

        return mapper.toResponse(savedFactura);
    }
}
