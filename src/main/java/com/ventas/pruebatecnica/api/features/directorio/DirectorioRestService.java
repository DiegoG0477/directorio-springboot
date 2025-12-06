package com.ventas.pruebatecnica.api.features.directorio;

import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaRequest;
import com.ventas.pruebatecnica.api.features.directorio.dto.PersonaResponse;
import com.ventas.pruebatecnica.api.features.directorio.services.DirectorioService;
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
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Directorio de Personas", description = "Operaciones para la gestión de personas")
public class DirectorioRestService {

    private final DirectorioService directorioService;

    @PostMapping
    @Operation(summary = "Crear una nueva persona")
    @ApiResponse(responseCode = "201", description = "Persona creada exitosamente")
    public ResponseEntity<PersonaResponse> crearPersona(@Valid @RequestBody PersonaRequest request) {

        PersonaResponse response = directorioService.storePersona(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{identificacion}")
    @Operation(summary = "Obtener una persona por su número de identificación")
    @ApiResponse(responseCode = "200", description = "Persona encontrada")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    public ResponseEntity<PersonaResponse> obtenerPorIdentificacion(@PathVariable String identificacion) {
        return ResponseEntity.ok(directorioService.findPersonaByIdentificacion(identificacion));
    }

    @GetMapping
    @Operation(summary = "Obtener un listado paginado de personas")
    @ApiResponse(responseCode = "200", description = "Listado de personas obtenido correctamente")
    public ResponseEntity<Page<PersonaResponse>> obtenerTodas(
            @Parameter(description = "Parámetros de paginación (size, page)")
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(directorioService.findPersonas(pageable));
    }

    @DeleteMapping("/{identificacion}")
    @Operation(summary = "Eliminar una persona por su número de identificación")
    @ApiResponse(responseCode = "204", description = "Persona eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    public ResponseEntity<Void> eliminarPersona(@PathVariable String identificacion) {

        directorioService.deletePersonaByIdentificacion(identificacion);

        return ResponseEntity.noContent().build();
    }
}
