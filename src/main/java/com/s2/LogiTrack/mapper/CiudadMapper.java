
package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Ciudad;
import com.s2.LogiTrack.dto.request.CiudadDtoRequest;
import com.s2.LogiTrack.dto.response.CiudadDtoResponse;
import org.springframework.stereotype.Component;

@Component

public class CiudadMapper {

    public CiudadDtoResponse entityToDto(Ciudad ciudad) {
        if (ciudad == null) return null;
        return new CiudadDtoResponse(ciudad.getId(),ciudad.getNombre(), ciudad.getDepartamento());
    }

    public Ciudad dtoToEntity(CiudadDtoRequest dto) {
        if (dto == null) return null;
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(dto.nombre());
        ciudad.setDepartamento(dto.departamento());
        return ciudad;
    }

    public void updateEntityToDto(Ciudad ciudad, CiudadDtoRequest dto) {
        if (dto == null || ciudad == null) return;
        ciudad.setNombre(dto.nombre());
        ciudad.setDepartamento(dto.departamento());
    }
}
