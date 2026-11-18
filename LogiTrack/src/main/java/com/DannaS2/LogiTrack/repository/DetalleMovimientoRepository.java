package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, Long> {
    List<DetalleMovimiento> findByMovimientoId(Long movimientoId);

    List<DetalleMovimiento> findByProductoId(Long productoId);

    @Query("""
    SELECT p.id, p.nombre, SUM(d.cantidad)
    FROM DetalleMovimiento d
    JOIN d.producto p
    GROUP BY p.id, p.nombre
    ORDER BY SUM(d.cantidad) DESC
""")
    List<Object[]> obtenerProductosMasMovidos(Pageable pageable);
}
