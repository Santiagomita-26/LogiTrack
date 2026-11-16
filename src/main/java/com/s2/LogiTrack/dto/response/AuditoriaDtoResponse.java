package com.s2.LogiTrack.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.s2.LogiTrack.model.Auditoria;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record AuditoriaDtoResponse(Long id, Auditoria.TipoOperacion operacion, LocalDateTime fecha, Long usuarioId, String usuarioNombre,
                                   String entidad, @JsonRawValue JsonNode valoresAnteriores, @JsonRawValue JsonNode valoresNuevos)
{
}
