package com.idat.biblioteca.service;

import com.idat.biblioteca.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    List<UsuarioResponse> listar();

    UsuarioResponse buscarPorId(Long id);

    UsuarioResponse buscarPorEmail(String email);
}