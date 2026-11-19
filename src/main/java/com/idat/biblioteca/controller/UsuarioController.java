package com.idat.biblioteca.controller;

import com.idat.biblioteca.dto.response.UsuarioResponse;
import com.idat.biblioteca.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {

        return ResponseEntity.ok(
                usuarioService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id)
        );
    }

    @GetMapping("/email")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                usuarioService.buscarPorEmail(email)
        );
    }
}