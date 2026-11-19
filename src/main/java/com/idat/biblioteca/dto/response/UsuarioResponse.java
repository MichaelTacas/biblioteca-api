package com.idat.biblioteca.dto.response;

import java.util.Set;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private Set<String> roles;

    public UsuarioResponse() {
    }

    public UsuarioResponse(
            Long id,
            String nombre,
            String email,
            Set<String> roles
    ) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}