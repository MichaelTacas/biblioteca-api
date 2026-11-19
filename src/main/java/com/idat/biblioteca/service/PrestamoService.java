package com.idat.biblioteca.service;

import com.idat.biblioteca.dto.request.PrestamoRequest;
import com.idat.biblioteca.dto.response.PrestamoResponse;
import com.idat.biblioteca.model.EstadoPrestamo;

import java.util.List;

public interface PrestamoService {

    PrestamoResponse registrar(PrestamoRequest request);

    List<PrestamoResponse> listar();

    PrestamoResponse buscarPorId(Long id);

    PrestamoResponse registrarDevolucion(Long id);

    List<PrestamoResponse> buscarPorUsuario(Long usuarioId);

    List<PrestamoResponse> buscarPorEstado(EstadoPrestamo estado);

    List<PrestamoResponse> buscarMisPrestamos(String email);
}