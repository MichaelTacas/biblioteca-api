package com.idat.biblioteca.controller;

import com.idat.biblioteca.dto.request.LoginRequest;
import com.idat.biblioteca.dto.request.RegistroUsuarioRequest;
import com.idat.biblioteca.dto.response.AuthResponse;
import com.idat.biblioteca.dto.response.UsuarioResponse;
import com.idat.biblioteca.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.idat.biblioteca.dto.request.RefreshTokenRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(
            @Valid @RequestBody RegistroUsuarioRequest request) {

        UsuarioResponse usuario = authService.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refrescarToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authService.refrescarToken(request)
        );
    }
}