package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    Ciudad findByNombre(String nombre);
    List<Ciudad> findByDepartamento(String departamento);
    boolean existsByNombre(String nombre);

}