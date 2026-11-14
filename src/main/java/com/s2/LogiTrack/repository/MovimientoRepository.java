package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByTipo(Movimiento.TipoMovimiento tipo);
    List<Movimiento> findByUsuarioId(Long usuarioId);
    List<Movimiento> findByProductoId(Long productoId);
    List<Movimiento> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Movimiento> findByBodegaOrigenId(Long bodegaId);
    List<Movimiento> findByBodegaDestinoId(Long bodegaId);
    List<Movimiento> findByUsuarioIdAndFechaBetween( Long usuarioId, LocalDateTime fechaInicio,LocalDateTime fechaFin
    );

}