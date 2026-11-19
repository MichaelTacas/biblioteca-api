package com.idat.biblioteca.service.impl;

import com.idat.biblioteca.dto.request.LibroRequest;
import com.idat.biblioteca.dto.response.LibroResponse;
import com.idat.biblioteca.exception.RecursoNoEncontradoException;
import com.idat.biblioteca.exception.ReglaNegocioException;
import com.idat.biblioteca.model.Libro;
import com.idat.biblioteca.repository.LibroRepository;
import com.idat.biblioteca.service.LibroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public LibroResponse registrar(LibroRequest request) {

        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new ReglaNegocioException(
                    "Ya existe un libro registrado con ese ISBN"
            );
        }

        Libro libro = new Libro();

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setIsbn(request.getIsbn());
        libro.setCategoria(request.getCategoria());

        libro.setDisponible(
                request.getDisponible() == null
                        || request.getDisponible()
        );

        Libro guardado = libroRepository.save(libro);

        return convertirResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> listar() {

        return libroRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse buscarPorId(Long id) {

        Libro libro = obtenerLibro(id);

        return convertirResponse(libro);
    }

    @Override
    @Transactional
    public LibroResponse actualizar(
            Long id,
            LibroRequest request) {

        Libro libro = obtenerLibro(id);

        libroRepository.findByIsbn(request.getIsbn())
                .filter(encontrado ->
                        !encontrado.getId().equals(id))
                .ifPresent(encontrado -> {
                    throw new ReglaNegocioException(
                            "Ya existe otro libro con ese ISBN"
                    );
                });

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setIsbn(request.getIsbn());
        libro.setCategoria(request.getCategoria());

        if (request.getDisponible() != null) {
            libro.setDisponible(request.getDisponible());
        }

        Libro actualizado = libroRepository.save(libro);

        return convertirResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Libro libro = obtenerLibro(id);

        libroRepository.delete(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> listarDisponibles() {

        return libroRepository.findByDisponibleTrue()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> buscarPorTitulo(String titulo) {

        return libroRepository
                .findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    private Libro obtenerLibro(Long id) {

        return libroRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Libro no encontrado"
                        )
                );
    }

    private LibroResponse convertirResponse(Libro libro) {

        return new LibroResponse(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getCategoria(),
                libro.isDisponible()
        );
    }
}