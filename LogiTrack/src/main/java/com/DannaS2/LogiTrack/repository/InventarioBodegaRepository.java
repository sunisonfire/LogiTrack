package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.InventarioBodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioBodegaRepository extends JpaRepository<InventarioBodega, Long> {

    List<InventarioBodega> findByProductoId(Long productoId);

    // (bodega_id, producto_id): devuelve 0 o 1 resultado
    Optional<InventarioBodega> findByBodegaIdAndProductoId(Long bodegaId, Long productoId);

    boolean existsByBodegaIdAndProductoId(Long bodegaId, Long productoId);

    List<InventarioBodega> findByCantidadLessThan(Integer limite);

    //EXAMEN
    //2.
    List<InventarioBodega> findByBodegaId(Long bodegaId);
}