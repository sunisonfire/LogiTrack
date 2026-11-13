package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    List<Bodega> findByCiudadId(Long ciudadId);

    List<Bodega> findByEncargadoId(Long encargadoId);

    List<Bodega> findByNombreContainingIgnoreCase(String texto);

    boolean existsByNombreIgnoreCaseAndCiudadId(String nombre, Long ciudadId);
}
