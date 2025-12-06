package com.ventas.pruebatecnica.api.features.directorio.services.impl;

import com.ventas.pruebatecnica.api.features.directorio.mappers.DirectorioMapper;
import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaRequest;
import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaResponse;
import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import com.ventas.pruebatecnica.api.features.directorio.repositories.PersonaRepository;
import com.ventas.pruebatecnica.api.features.directorio.services.DirectorioService;
import com.ventas.pruebatecnica.api.shared.exceptions.ResourceAlreadyExistsException;
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
public class DirectorioServiceImpl implements DirectorioService {

    private final PersonaRepository personaRepository;
    private final DirectorioMapper mapper;

    @Override
    public PersonaResponse findPersonaByIdentificacion(String identificacion) {
        log.info("Searching for persona with identification: {}", identificacion);
        return personaRepository.findByIdentificacion(identificacion)
                .map(mapper::toResponse)
                .orElseThrow(() -> {
                    log.warn("Persona not found with identification: {}", identificacion);
                    return new ResourceNotFoundException("Persona not found with identification: " + identificacion);
                });
    }

    @Override
    public Page<PersonaResponse> findPersonas(Pageable pageable) {
        log.info("Fetching a page of personas. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<PersonaResponse> personas = personaRepository.findAll(pageable).map(mapper::toResponse);
        log.info("Found {} personas in total.", personas.getTotalElements());
        return personas;
    }

    @Override
    @Transactional
    public void deletePersonaByIdentificacion(String identificacion) {
        log.info("Attempting to delete persona and associated data for identification: {}", identificacion);

        if (!personaRepository.existsByIdentificacion(identificacion)) {
            log.warn("Cannot delete. Persona not found with identification: {}", identificacion);
            throw new ResourceNotFoundException("Cannot delete: Persona not found with identification " + identificacion);
        }

        personaRepository.deleteByIdentificacion(identificacion);
        log.info("Successfully deleted persona with identification: {}", identificacion);
    }

    @Override
    public PersonaResponse storePersona(PersonaRequest request) {
        log.info("Saving new persona with identification: {}", request.getIdentificacion());

        if (personaRepository.existsByIdentificacion(request.getIdentificacion())) {
            log.warn("Attempted to create a persona with a duplicate identification: {}", request.getIdentificacion());
            throw new ResourceAlreadyExistsException("A persona with the identification '" + request.getIdentificacion() + "' already exists.");
        }

        Persona persona = mapper.toEntity(request);
        Persona savedPersona = personaRepository.save(persona);
        log.info("Successfully saved new persona with ID: {} and identification: {}", savedPersona.getId(), savedPersona.getIdentificacion());

        return mapper.toResponse(savedPersona);
    }
}
