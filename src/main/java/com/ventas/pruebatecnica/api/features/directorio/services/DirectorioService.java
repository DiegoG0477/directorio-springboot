package com.ventas.pruebatecnica.api.features.directorio.services;

import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaRequest;
import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DirectorioService {

    PersonaResponse findPersonaByIdentificacion(String identificacion);

    Page<PersonaResponse> findPersonas(Pageable pageable);

    void deletePersonaByIdentificacion(String identificacion);

    PersonaResponse storePersona(PersonaRequest request);
}