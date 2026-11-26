package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Categoria;
import com.DannaS2.LogiTrack.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);

    List<Producto> findByMarcaId(Long marcaId);

    //EXAMEN
    //3.
    List<Producto> findByNombreContainingIgnoreCase(String texto);
}

