package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.dto.request.UsuarioActualizarDtoRequest;
import com.s2.LogiTrack.model.Usuario;
import com.s2.LogiTrack.dto.request.UsuarioRegistroDtoRequest;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import org.springframework.stereotype.Component;


@Component
public class UsuarioMapper {

    public UsuarioDtoResponse entityToDto(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDtoResponse(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol(), usuario.getEstado());
    }

    public Usuario dtoToEntity(UsuarioRegistroDtoRequest dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setCorreo(dto.correo());
        usuario.setPassword(dto.password());
        usuario.setRol(Usuario.Rol.USUARIO);
        usuario.setEstado(Usuario.Estado.PENDIENTE);

        return usuario;
    }

    public void updateEntityToDto(Usuario usuario, UsuarioActualizarDtoRequest dto) {
        if (dto == null || usuario == null) return;
        usuario.setNombre(dto.nombre());
        usuario.setCorreo(dto.correo());
    }
}