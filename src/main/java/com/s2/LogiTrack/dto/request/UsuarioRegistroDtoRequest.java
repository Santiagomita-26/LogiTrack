package com.s2.LogiTrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRegistroDtoRequest(

        @NotBlank @Size(min = 2, max = 100) String nombre ,
        @NotBlank  @Email @Size  (max = 100)  String correo,
        @NotBlank @Size (min = 8, max = 100) String password
) {
}
