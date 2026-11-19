package com.idat.biblioteca.service;

import com.idat.biblioteca.dto.request.LoginRequest;
import com.idat.biblioteca.dto.request.RefreshTokenRequest;
import com.idat.biblioteca.dto.request.RegistroUsuarioRequest;
import com.idat.biblioteca.dto.response.AuthResponse;
import com.idat.biblioteca.dto.response.UsuarioResponse;

public interface AuthService {

    UsuarioResponse registrar(RegistroUsuarioRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refrescarToken(RefreshTokenRequest request);
}