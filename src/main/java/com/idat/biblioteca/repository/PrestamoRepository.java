package com.idat.biblioteca.repository;

import com.idat.biblioteca.model.EstadoPrestamo;
import com.idat.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    boolean existsByLibroIdAndEstado(
            Long libroId,
            EstadoPrestamo estado


    );

    @Query("""
        SELECT p
        FROM Prestamo p
        JOIN FETCH p.usuario u
        JOIN FETCH p.libro l
        WHERE u.id = :usuarioId
        ORDER BY p.fechaPrestamo DESC
        """)
    List<Prestamo> buscarHistorialPorUsuario(
            @Param("usuarioId") Long usuarioId
    );
}