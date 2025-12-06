package com.ventas.pruebatecnica.api.features.ventas.mappers;

import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaRequest;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaResponse;
import com.ventas.pruebatecnica.api.features.ventas.entities.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VentasMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    Factura toEntity(FacturaRequest request);

    FacturaResponse toResponse(Factura entity);
}