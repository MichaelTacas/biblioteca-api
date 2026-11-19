package com.idat.biblioteca.dto.response;

public class AuthResponse {

    private String token;
    private String refreshToken;
    private String tipo;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(
            String token,
            String refreshToken,
            String tipo,
            String email) {

        this.token = token;
        this.refreshToken = refreshToken;
        this.tipo = tipo;
        this.email = email;
    }

    // Constructor temporal compatible con el login actual
    public AuthResponse(
            String token,
            String tipo,
            String email) {

        this.token = token;
        this.tipo = tipo;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}