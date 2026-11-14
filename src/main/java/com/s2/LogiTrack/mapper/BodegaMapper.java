package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Bodega;
import com.s2.LogiTrack.model.Ciudad;
import com.s2.LogiTrack.dto.request.BodegaDtoRequest;
import com.s2.LogiTrack.dto.response.BodegaDtoResponse;

public class BodegaMapper {

    public BodegaDtoResponse entityToDto(Bodega bodega) {
        if (bodega == null || bodega.getCiudad() == null) return null;
        return new BodegaDtoResponse(bodega.getId(), bodega.getNombre(), bodega.getDireccion(), bodega.getCapacidad(),
                   bodega.getCiudad().getId(), bodega.getCiudad().getNombre());
    }

    public Bodega dtoToEntity(BodegaDtoRequest dto, Ciudad ciudad) {
        if (dto == null || ciudad == null) return null;
        Bodega bodega = new Bodega();
        bodega.setNombre(dto.nombre());
        bodega.setDireccion(dto.direccion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setCiudad(ciudad);

        return bodega;
    }

    public void updateEntityToDto(Bodega bodega, BodegaDtoRequest dto, Ciudad ciudad) {
        if (bodega == null || dto == null || ciudad == null) return;
        bodega.setNombre(dto.nombre());
        bodega.setDireccion(dto.direccion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setCiudad(ciudad);
    }
}