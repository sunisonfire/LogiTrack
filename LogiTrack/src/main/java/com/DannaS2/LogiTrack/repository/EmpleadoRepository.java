package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository {
    Optional<Empleado> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Empleado> findByBodegaId(Long bodegaId);
}
