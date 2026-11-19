package com.idat.biblioteca.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PrestamoRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser mayor a cero")
    private Long usuarioId;

    @NotNull(message = "El ID del libro es obligatorio")
    @Positive(message = "El ID del libro debe ser mayor a cero")
    private Long libroId;

    public PrestamoRequest() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }
}