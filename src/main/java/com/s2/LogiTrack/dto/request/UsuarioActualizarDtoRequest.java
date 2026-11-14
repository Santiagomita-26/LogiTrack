package com.s2.LogiTrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioActualizarDtoRequest(

        @NotBlank @Size(min = 2, max = 150)String nombre,
        @NotBlank @Email@Size(max = 150) String correo

) {
}