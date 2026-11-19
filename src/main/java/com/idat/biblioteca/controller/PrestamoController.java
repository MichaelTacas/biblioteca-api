package com.idat.biblioteca.controller;

import com.idat.biblioteca.dto.request.PrestamoRequest;
import com.idat.biblioteca.dto.response.PrestamoResponse;
import com.idat.biblioteca.model.EstadoPrestamo;
import com.idat.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<PrestamoResponse> registrar(
            @Valid @RequestBody PrestamoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prestamoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listar() {

        return ResponseEntity.ok(
                prestamoService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponse> registrarDevolucion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.registrarDevolucion(id)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PrestamoResponse>> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                prestamoService.buscarPorUsuario(usuarioId)
        );
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PrestamoResponse>> buscarPorEstado(
            @PathVariable EstadoPrestamo estado) {

        return ResponseEntity.ok(
                prestamoService.buscarPorEstado(estado)
        );
    }

    @GetMapping("/mis-prestamos")
    public ResponseEntity<List<PrestamoResponse>> misPrestamos(
            Authentication authentication) {

        return ResponseEntity.ok(
                prestamoService.buscarMisPrestamos(
                        authentication.getName()
                )
        );
    }
}