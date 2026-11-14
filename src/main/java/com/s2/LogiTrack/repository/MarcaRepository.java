package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Marca findByNombre(String nombre);
    boolean existsByNombre(String nombre);

}