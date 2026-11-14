package com.s2.LogiTrack.service;

import com.s2.LogiTrack.dto.request.UsuarioRegistroDtoRequest;
import com.s2.LogiTrack.dto.request.UsuarioActualizarDtoRequest;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import java.util.List;

public interface UsuarioService {

    UsuarioDtoResponse guardar(UsuarioRegistroDtoRequest dto);
    UsuarioDtoResponse actualizar(UsuarioActualizarDtoRequest dto, Long id);
    void eliminar(Long id);
    List<UsuarioDtoResponse> listar();
    UsuarioDtoResponse buscarId(Long id);
    UsuarioDtoResponse buscarCorreo(String correo);
    List<UsuarioDtoResponse> listarPendientes();
    UsuarioDtoResponse aprobar(Long id);
    UsuarioDtoResponse rechazar(Long id);

}