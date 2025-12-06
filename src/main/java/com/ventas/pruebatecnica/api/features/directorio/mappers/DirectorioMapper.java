package com.ventas.pruebatecnica.api.features.directorio.mappers;

import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaRequest;
import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaResponse;
import com.ventas.pruebatecnica.api.features.directorio.entities.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DirectorioMapper {

    Persona toEntity(PersonaRequest request);

    PersonaResponse toResponse(Persona entity);
}