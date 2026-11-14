package com.s2.LogiTrack.dto.response;

import com.s2.LogiTrack.model.Usuario;

public record UsuarioDtoResponse (Long id , String nombre , String correo , Usuario.Rol rol , Usuario.Estado estado ){
}
