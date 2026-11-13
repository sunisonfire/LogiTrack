package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Persona;
import com.DannaS2.LogiTrack.model.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Persona> findByTipoPersona(TipoPersona tipoPersona);
}

