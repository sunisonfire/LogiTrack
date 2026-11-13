package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Auditoria;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByPersonaIdOrderByFechaHoraDesc(Long personaId);

    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);

    List<Auditoria> findByEntidadAndEntidadIdOrderByFechaHoraDesc(String entidad, Long entidadId);

    List<Auditoria> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
