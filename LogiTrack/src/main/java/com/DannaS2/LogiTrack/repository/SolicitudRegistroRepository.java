
package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.EstadoSolicitud;
import com.DannaS2.LogiTrack.model.SolicitudRegistro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRegistroRepository
        extends JpaRepository<SolicitudRegistro, Long> {

    List<SolicitudRegistro> findByEstado(EstadoSolicitud estado);

    boolean existsByDocumento(String documento);

    boolean existsByUsername(String username);

    List<SolicitudRegistro> findByEstadoOrderByFechaSolicitudAsc(
            EstadoSolicitud estado
    );
}