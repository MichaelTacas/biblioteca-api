package com.idat.biblioteca.service.impl;

import com.idat.biblioteca.dto.request.PrestamoRequest;
import com.idat.biblioteca.dto.response.PrestamoResponse;
import com.idat.biblioteca.exception.RecursoNoEncontradoException;
import com.idat.biblioteca.exception.ReglaNegocioException;
import com.idat.biblioteca.model.EstadoPrestamo;
import com.idat.biblioteca.model.Libro;
import com.idat.biblioteca.model.Prestamo;
import com.idat.biblioteca.model.Usuario;
import com.idat.biblioteca.repository.LibroRepository;
import com.idat.biblioteca.repository.PrestamoRepository;
import com.idat.biblioteca.repository.UsuarioRepository;
import com.idat.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public PrestamoServiceImpl(
            PrestamoRepository prestamoRepository,
            UsuarioRepository usuarioRepository,
            LibroRepository libroRepository) {

        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public PrestamoResponse registrar(PrestamoRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado"
                        )
                );

        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Libro no encontrado"
                        )
                );

        boolean tienePrestamoActivo =
                prestamoRepository.existsByLibroIdAndEstado(
                        libro.getId(),
                        EstadoPrestamo.PRESTADO
                );

        if (!libro.isDisponible() || tienePrestamoActivo) {
            throw new ReglaNegocioException(
                    "El libro no se encuentra disponible para préstamo"
            );
        }

        Prestamo prestamo = new Prestamo(usuario, libro);

        libro.setDisponible(false);
        libroRepository.save(libro);

        Prestamo guardado = prestamoRepository.save(prestamo);

        return convertirResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> listar() {

        return prestamoRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponse buscarPorId(Long id) {

        Prestamo prestamo = obtenerPrestamo(id);

        return convertirResponse(prestamo);
    }

    @Override
    @Transactional
    public PrestamoResponse registrarDevolucion(Long id) {

        Prestamo prestamo = obtenerPrestamo(id);

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ReglaNegocioException(
                    "El préstamo ya fue devuelto"
            );
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);

        libroRepository.save(libro);

        Prestamo actualizado = prestamoRepository.save(prestamo);

        return convertirResponse(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> buscarPorUsuario(Long usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException(
                    "Usuario no encontrado"
            );
        }

        return prestamoRepository.buscarHistorialPorUsuario(usuarioId)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> buscarPorEstado(
            EstadoPrestamo estado) {

        return prestamoRepository.findByEstado(estado)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> buscarMisPrestamos(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado"
                        )
                );

        return prestamoRepository
                .findByUsuarioId(usuario.getId())
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    private Prestamo obtenerPrestamo(Long id) {

        return prestamoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Préstamo no encontrado"
                        )
                );
    }

    private PrestamoResponse convertirResponse(
            Prestamo prestamo) {

        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getUsuario().getId(),
                prestamo.getUsuario().getNombre(),
                prestamo.getLibro().getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.getEstado()
        );
    }
}