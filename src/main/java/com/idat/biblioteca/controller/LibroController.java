package com.idat.biblioteca.controller;

import com.idat.biblioteca.dto.request.LibroRequest;
import com.idat.biblioteca.dto.response.LibroResponse;
import com.idat.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<LibroResponse> registrar(
            @Valid @RequestBody LibroRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(libroService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<LibroResponse>> listar() {

        return ResponseEntity.ok(
                libroService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                libroService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequest request) {

        return ResponseEntity.ok(
                libroService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        libroService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LibroResponse>> listarDisponibles() {

        return ResponseEntity.ok(
                libroService.listarDisponibles()
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LibroResponse>> buscarPorTitulo(
            @RequestParam String titulo) {

        return ResponseEntity.ok(
                libroService.buscarPorTitulo(titulo)
        );
    }
}