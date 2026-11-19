package com.idat.biblioteca.service;

import com.idat.biblioteca.dto.request.LibroRequest;
import com.idat.biblioteca.dto.response.LibroResponse;

import java.util.List;

public interface LibroService {

    LibroResponse registrar(LibroRequest request);

    List<LibroResponse> listar();

    LibroResponse buscarPorId(Long id);

    LibroResponse actualizar(Long id, LibroRequest request);

    void eliminar(Long id);

    List<LibroResponse> listarDisponibles();

    List<LibroResponse> buscarPorTitulo(String titulo);
}