package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    List<Bodega> findByCiudadId(Long ciudadId);

    List<Bodega> findByNombreContainingIgnoreCase(String texto);

    boolean existsByNombreIgnoreCaseAndCiudadId(String nombre, Long ciudadId);

    @Query("""
    SELECT b.id, b.nombre, COALESCE(SUM(i.cantidad), 0)
    FROM Bodega b
    LEFT JOIN InventarioBodega i ON i.bodega = b
    GROUP BY b.id, b.nombre
    ORDER BY b.nombre
""")
    List<Object[]> obtenerStockTotalPorBodega();

    //EXAMEN
    List<Bodega> findByEncargadoId(Long encargadoId);


}
