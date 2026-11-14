package com.s2.LogiTrack.repository;

import com.s2.LogiTrack.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByUsuarioId(Long usuarioId);
    List<Auditoria> findByOperacion(Auditoria.TipoOperacion operacion);
    List<Auditoria> findByEntidad(String entidad);
    List<Auditoria> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Auditoria> findByUsuarioIdAndOperacion(Long usuarioId, Auditoria.TipoOperacion operacion);
    List<Auditoria> findByUsuarioIdAndFechaBetween( Long usuarioId, LocalDateTime fechaInicio,LocalDateTime fechaFin);

}