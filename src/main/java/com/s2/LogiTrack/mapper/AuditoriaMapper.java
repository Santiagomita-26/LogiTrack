package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.dto.response.AuditoriaDtoResponse;

public class AuditoriaMapper {

    public AuditoriaDtoResponse entityToDto(Auditoria auditoria) {

        if (auditoria == null) return null;
        return new AuditoriaDtoResponse( auditoria.getId(),auditoria.getOperacion(), auditoria.getFecha(),
                auditoria.getUsuario().getId(),auditoria.getUsuario().getNombre(),auditoria.getEntidad(),
                auditoria.getValoresAnteriores(), auditoria.getValoresNuevos() );
    }
}