package com.s2.LogiTrack.service;

import com.s2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.model.Movimiento;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {

    MovimientoDtoResponse guardar(MovimientoDtoRequest dto);
    List<MovimientoDtoResponse> listar();
    MovimientoDtoResponse buscarId(Long id);
    List<MovimientoDtoResponse> buscarTipo(Movimiento.TipoMovimiento tipo);
    List<MovimientoDtoResponse> buscarUsuarioId(Long id);
    List<MovimientoDtoResponse> buscarProductoId(Long id);
    List<MovimientoDtoResponse> buscarFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoDtoResponse> buscarBodegaOrigenId(Long id);
    List<MovimientoDtoResponse> buscarBodegaDestinoId(Long id);
    List<MovimientoDtoResponse> buscarUsuarioFecha(Long usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

}