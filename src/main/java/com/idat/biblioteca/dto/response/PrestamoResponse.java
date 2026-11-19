package com.idat.biblioteca.dto.response;

import com.idat.biblioteca.model.EstadoPrestamo;

import java.time.LocalDate;

public class PrestamoResponse {

    private Long id;

    private Long usuarioId;
    private String usuarioNombre;

    private Long libroId;
    private String libroTitulo;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    private EstadoPrestamo estado;

    public PrestamoResponse() {
    }

    public PrestamoResponse(
            Long id,
            Long usuarioId,
            String usuarioNombre,
            Long libroId,
            String libroTitulo,
            LocalDate fechaPrestamo,
            LocalDate fechaDevolucion,
            EstadoPrestamo estado
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.libroId = libroId;
        this.libroTitulo = libroTitulo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getLibroTitulo() {
        return libroTitulo;
    }

    public void setLibroTitulo(String libroTitulo) {
        this.libroTitulo = libroTitulo;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}