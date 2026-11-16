package com.s2.LogiTrack.service.interfaces;

import com.s2.LogiTrack.dto.request.UsuarioLoginDtoRequest;
import com.s2.LogiTrack.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(UsuarioLoginDtoRequest dto);

}