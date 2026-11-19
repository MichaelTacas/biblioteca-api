package com.idat.biblioteca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LibroRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 2, max = 150, message = "El título debe tener entre 2 y 150 caracteres")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 2, max = 100, message = "El autor debe tener entre 2 y 100 caracteres")
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(
            regexp = "^[0-9-]{10,17}$",
            message = "El ISBN debe tener un formato válido"
    )
    private String isbn;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 80, message = "La categoría no puede superar los 80 caracteres")
    private String categoria;

    private Boolean disponible;

    public LibroRequest() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}