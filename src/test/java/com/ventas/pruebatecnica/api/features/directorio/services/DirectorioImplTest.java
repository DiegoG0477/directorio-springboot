package com.ventas.pruebatecnica.api.features.directorio.services;

import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaRequest;
import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaResponse;
import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import com.ventas.pruebatecnica.api.features.directorio.mappers.DirectorioMapper;
import com.ventas.pruebatecnica.api.features.directorio.repositories.PersonaRepository;
import com.ventas.pruebatecnica.api.features.directorio.services.impl.DirectorioServiceImpl;
import com.ventas.pruebatecnica.api.shared.exceptions.ResourceAlreadyExistsException;
import com.ventas.pruebatecnica.api.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectorioImplTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private DirectorioMapper mapper;

    @InjectMocks
    private DirectorioServiceImpl directorioService;

    @Test
    void storePersona_shouldSaveAndReturnPersona_whenIdentificationIsUnique() {
        PersonaRequest request = new PersonaRequest();
        request.setIdentificacion("12345");
        request.setNombre("Diego");

        Persona personaToSave = new Persona();
        Persona savedPersona = new Persona();
        savedPersona.setId(1L);
        savedPersona.setIdentificacion("12345");

        PersonaResponse expectedResponse = new PersonaResponse();
        expectedResponse.setId(1L);
        expectedResponse.setIdentificacion("12345");

        when(personaRepository.existsByIdentificacion("12345")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(personaToSave);
        when(personaRepository.save(personaToSave)).thenReturn(savedPersona);
        when(mapper.toResponse(savedPersona)).thenReturn(expectedResponse);

        PersonaResponse actualResponse = directorioService.storePersona(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        verify(personaRepository).existsByIdentificacion("12345");
        verify(personaRepository).save(personaToSave);
    }

    @Test
    void storePersona_shouldThrowException_whenIdentificationAlreadyExists() {
        PersonaRequest request = new PersonaRequest();
        request.setIdentificacion("54321");

        when(personaRepository.existsByIdentificacion("54321")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> directorioService.storePersona(request));

        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    void findPersonaByIdentificacion_shouldReturnPersona_whenPersonaExists() {
        String existingId = "111";
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setIdentificacion(existingId);

        PersonaResponse expectedResponse = new PersonaResponse();
        expectedResponse.setId(1L);
        expectedResponse.setIdentificacion(existingId);

        when(personaRepository.findByIdentificacion(existingId)).thenReturn(Optional.of(persona));
        when(mapper.toResponse(persona)).thenReturn(expectedResponse);

        PersonaResponse actualResponse = directorioService.findPersonaByIdentificacion(existingId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        verify(personaRepository).findByIdentificacion(existingId);
    }

    @Test
    void findPersonaByIdentificacion_shouldThrowNotFoundException_whenPersonaDoesNotExist() {
        String nonExistentId = "12345";
        when(personaRepository.findByIdentificacion(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> directorioService.findPersonaByIdentificacion(nonExistentId));
    }

    @Test
    void deletePersonaByIdentificacion_shouldCallRepositoryDelete_whenPersonaExists() {
        String existingId = "999";
        when(personaRepository.existsByIdentificacion(existingId)).thenReturn(true);
        doNothing().when(personaRepository).deleteByIdentificacion(existingId);

        directorioService.deletePersonaByIdentificacion(existingId);

        verify(personaRepository).existsByIdentificacion(existingId);
        verify(personaRepository).deleteByIdentificacion(existingId);
    }

    @Test
    void deletePersonaByIdentificacion_shouldThrowNotFoundException_whenPersonaDoesNotExist() {
        String nonExistentId = "non-existent";
        when(personaRepository.existsByIdentificacion(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> directorioService.deletePersonaByIdentificacion(nonExistentId));

        verify(personaRepository).existsByIdentificacion(nonExistentId);
        verify(personaRepository, never()).deleteByIdentificacion(anyString());
    }
}
