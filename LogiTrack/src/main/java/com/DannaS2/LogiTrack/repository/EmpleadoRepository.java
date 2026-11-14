package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Empleado> findByBodegaId(Long bodegaId);
}
