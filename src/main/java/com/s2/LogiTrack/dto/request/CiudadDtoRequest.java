package com.s2.LogiTrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CiudadDtoRequest (

        @NotBlank @Size (min = 2, max = 100) String nombre ,
        @NotBlank @Size(min = 2, max = 100)  String departamento

) {
}
