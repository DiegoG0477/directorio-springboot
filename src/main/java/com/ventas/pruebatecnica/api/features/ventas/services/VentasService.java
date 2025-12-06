package com.ventas.pruebatecnica.api.features.ventas.services;

import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaRequest;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentasService {

    Page<FacturaResponse> findFacturasByPersona(String identificacion, Pageable pageable);

    FacturaResponse storeFactura(FacturaRequest request);
}
