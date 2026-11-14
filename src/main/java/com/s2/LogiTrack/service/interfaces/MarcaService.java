package com.s2.LogiTrack.service.interfaces;

import com.s2.LogiTrack.dto.request.MarcaDtoRequest;
import com.s2.LogiTrack.dto.response.MarcaDtoResponse;

import java.util.List;

public interface MarcaService {

    MarcaDtoResponse guardar(MarcaDtoRequest dto);
    MarcaDtoResponse actualizar(MarcaDtoRequest dto, Long id);
    void eliminar(Long id);
    List<MarcaDtoResponse> listar();
    MarcaDtoResponse buscarId(Long id);

}