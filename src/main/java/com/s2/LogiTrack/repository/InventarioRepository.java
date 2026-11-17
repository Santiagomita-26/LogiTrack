package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Inventario findByBodegaIdAndProductoId(Long bodegaId, Long productoId);
    List<Inventario> findByBodegaId(Long bodegaId);
    List<Inventario> findByProductoId(Long productoId);
    List<Inventario> findByStockLessThan(Integer stock);
    List<Inventario> findByStockLessThanEqual(Integer stock);
    List<Inventario> findByStockGreaterThan(Integer stock);

}