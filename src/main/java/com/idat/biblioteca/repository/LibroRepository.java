package com.idat.biblioteca.repository;

import com.idat.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Libro> findByDisponibleTrue();

    List<Libro> findByTituloContainingIgnoreCase(String titulo);
}