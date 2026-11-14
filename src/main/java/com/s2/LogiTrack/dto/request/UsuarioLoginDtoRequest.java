package com.s2.LogiTrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginDtoRequest(

        @NotBlank @Email String correo,
        @NotBlank String password

) {

}