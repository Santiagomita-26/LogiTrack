package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Movimiento;
import com.s2.LogiTrack.model.Usuario;
import com.s2.LogiTrack.model.Producto;
import com.s2.LogiTrack.model.Bodega;
import com.s2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import org.springframework.stereotype.Component;

@Component

public class MovimientoMapper {

    public MovimientoDtoResponse entityToDto(Movimiento movimiento) {

        if (movimiento == null) return null;

        return new MovimientoDtoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getProducto().getId(),
                movimiento.getProducto().getNombre(),
                movimiento.getUsuario().getId(),
                movimiento.getUsuario().getNombre(),
                movimiento.getBodegaOrigen() != null ? movimiento.getBodegaOrigen().getId() : null,
                movimiento.getBodegaOrigen() != null ? movimiento.getBodegaOrigen().getNombre() : null,
                movimiento.getBodegaDestino() != null ? movimiento.getBodegaDestino().getId() : null,
                movimiento.getBodegaDestino() != null? movimiento.getBodegaDestino().getNombre() : null
        );
    }

    public Movimiento dtoToEntity( MovimientoDtoRequest dto, Usuario usuario,Producto producto, Bodega bodegaOrigen,Bodega bodegaDestino) {
        if (dto == null || usuario == null || producto == null) return null;
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(dto.tipo());
        movimiento.setCantidad(dto.cantidad());
        movimiento.setFecha(java.time.LocalDateTime.now());
        movimiento.setUsuario(usuario);
        movimiento.setProducto(producto);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);

        return movimiento;
    }

    public void updateEntityToDto( Movimiento movimiento,MovimientoDtoRequest dto, Usuario usuario, Producto producto,
            Bodega bodegaOrigen, Bodega bodegaDestino) {

        if (movimiento == null || dto == null || usuario == null || producto == null) return;
        movimiento.setTipo(dto.tipo());
        movimiento.setCantidad(dto.cantidad());
        movimiento.setUsuario(usuario);
        movimiento.setProducto(producto);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
    }
}