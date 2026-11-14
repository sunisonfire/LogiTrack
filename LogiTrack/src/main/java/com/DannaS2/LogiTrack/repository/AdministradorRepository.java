package com.DannaS2.LogiTrack.repository;

import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByUsername(String username);

    boolean existsByUsername(String username);
}
