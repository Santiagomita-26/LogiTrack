package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {

    List<Bodega> findByCiudadId(Long ciudadId);
    Bodega findByNombre(String nombre);
    boolean existsByNombreAndCiudadId(String nombre, Long ciudadId);

}