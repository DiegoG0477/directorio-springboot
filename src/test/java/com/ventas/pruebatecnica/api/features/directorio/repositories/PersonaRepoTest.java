package com.ventas.pruebatecnica.api.features.directorio.repositories;

import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonaRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonaRepository personaRepository;

    private static final String PERSONA_ID = "123456789012345678";

    @BeforeEach
    void setUp() {
        Persona persona = new Persona();
        persona.setNombre("Diego");
        persona.setApellidoPaterno("Github");
        persona.setIdentificacion(PERSONA_ID);
        entityManager.persistAndFlush(persona);
    }

    @Test
    void findByIdentificacion_shouldReturnPersona_whenPersonaExists() {
        Optional<Persona> found = personaRepository.findByIdentificacion(PERSONA_ID);

        assertThat(found).isPresent();
        assertThat(found.get().getIdentificacion()).isEqualTo(PERSONA_ID);
    }

    @Test
    void findByIdentificacion_shouldReturnEmpty_whenPersonaDoesNotExist() {
        Optional<Persona> found = personaRepository.findByIdentificacion("non-existent-id");

        assertThat(found).isNotPresent();
    }

    @Test
    void existsByIdentificacion_shouldReturnTrue_whenPersonaExists() {
        boolean exists = personaRepository.existsByIdentificacion(PERSONA_ID);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByIdentificacion_shouldReturnFalse_whenPersonaDoesNotExist() {
        boolean exists = personaRepository.existsByIdentificacion("non-existent-id");

        assertThat(exists).isFalse();
    }

    @Test
    @Transactional
    void deleteByIdentificacion_shouldRemovePersona() {
        personaRepository.deleteByIdentificacion(PERSONA_ID);
        entityManager.flush(); // this ensures delete is executed
        entityManager.clear();

        Optional<Persona> found = personaRepository.findByIdentificacion(PERSONA_ID);
        assertThat(found).isNotPresent();
    }
}
