package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByMarcaId(Long marcaId);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoria(String categoria);
    boolean existsByNombreAndMarcaId(String nombre, Long marcaId);

}