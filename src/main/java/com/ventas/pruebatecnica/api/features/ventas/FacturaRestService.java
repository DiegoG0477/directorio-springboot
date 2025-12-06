package com.ventas.pruebatecnica.api.features.ventas;

import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaRequest;
import com.ventas.pruebatecnica.api.features.ventas.dto.FacturaResponse;
import com.ventas.pruebatecnica.api.features.ventas.services.VentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facturas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestión de Facturas", description = "Operaciones para la creación y consulta de facturas")
public class FacturaRestService {

    private final VentasService ventasService;

    @PostMapping
    @Operation(summary = "Crear una nueva factura")
    @ApiResponse(responseCode = "201", description = "Factura creada exitosamente")
    public ResponseEntity<FacturaResponse> crearFactura(@Valid @RequestBody FacturaRequest request) {

        FacturaResponse response = ventasService.storeFactura(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/por-cliente/{identificacion}")
    @Operation(summary = "Obtener un listado paginado de facturas por cliente")
    @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido correctamente")
    public ResponseEntity<Page<FacturaResponse>> obtenerFacturasPorCliente(
            @PathVariable String identificacion,
            @Parameter(description = "Parámetros de paginación (size, page)")
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(ventasService.findFacturasByPersona(identificacion, pageable));
    }
}
