package com.s2.LogiTrack.service.interfaces;

import com.s2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.s2.LogiTrack.model.Auditoria;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaService {

    List<AuditoriaDtoResponse> listar();
    AuditoriaDtoResponse buscarId(Long id);
    List<AuditoriaDtoResponse> buscarUsuarioId(Long id);
    List<AuditoriaDtoResponse> buscarOperacion(Auditoria.TipoOperacion operacion);
    List<AuditoriaDtoResponse> buscarEntidad(String entidad);
    List<AuditoriaDtoResponse> buscarFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<AuditoriaDtoResponse> buscarUsuarioOperacion(Long usuarioId, Auditoria.TipoOperacion operacion);
    List<AuditoriaDtoResponse> buscarUsuarioFecha(Long usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

}