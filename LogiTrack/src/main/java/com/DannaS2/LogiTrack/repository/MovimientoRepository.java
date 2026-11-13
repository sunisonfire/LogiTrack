package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Movimiento;
import com.DannaS2.LogiTrack.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByTipo(TipoMovimiento tipo);
    // el campo se llama "tipo"
    List<Movimiento> findByPersonaId(Long personaId);

    List<Movimiento> findByBodegaOrigenId(Long bodegaId);

    List<Movimiento> findByBodegaDestinoId(Long bodegaId);

    List<Movimiento> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}
