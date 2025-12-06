package com.ventas.pruebatecnica.api.features.ventas.repositories;

import com.ventas.pruebatecnica.api.features.ventas.entities.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Page<Factura> findByPersona_Identificacion(String identificacion, Pageable pageable);
}
