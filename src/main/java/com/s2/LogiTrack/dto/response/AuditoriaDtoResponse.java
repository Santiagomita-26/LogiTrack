package com.s2.LogiTrack.dto.response;

import com.s2.LogiTrack.model.Auditoria;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record AuditoriaDtoResponse(Long id, Auditoria.TipoOperacion operacion, LocalDateTime fecha, Long usuarioId, String usuarioNombre,
                                   String entidad, JsonNode valoresAnteriores, JsonNode valoresNuevos)
{
}
