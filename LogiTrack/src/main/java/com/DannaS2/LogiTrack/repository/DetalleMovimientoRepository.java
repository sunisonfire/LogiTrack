package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, Long> {
    List<DetalleMovimiento> findByMovimientoId(Long movimientoId);

    List<DetalleMovimiento> findByProductoId(Long productoId);
}
