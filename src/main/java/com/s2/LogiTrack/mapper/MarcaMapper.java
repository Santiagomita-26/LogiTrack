package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Marca;
import com.s2.LogiTrack.dto.request.MarcaDtoRequest;
import com.s2.LogiTrack.dto.response.MarcaDtoResponse;
import org.springframework.stereotype.Component;

@Component

public class MarcaMapper {

    public MarcaDtoResponse entityToDto(Marca marca) {
        if (marca == null) return null;
        return new MarcaDtoResponse(marca.getId(),marca.getNombre());
    }

    public Marca dtoToEntity(MarcaDtoRequest dto) {
        if (dto == null) return null;
        Marca marca = new Marca();
        marca.setNombre(dto.nombre());

        return marca;
    }

    public void updateEntityToDto(Marca marca, MarcaDtoRequest dto) {
        if (dto == null || marca == null) return;
        marca.setNombre(dto.nombre());
    }
}