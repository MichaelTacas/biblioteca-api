package com.idat.biblioteca.service.impl;

import com.idat.biblioteca.dto.response.UsuarioResponse;
import com.idat.biblioteca.exception.RecursoNoEncontradoException;
import com.idat.biblioteca.model.Usuario;
import com.idat.biblioteca.repository.UsuarioRepository;
import com.idat.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado"
                        )
                );

        return convertirResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado"
                        )
                );

        return convertirResponse(usuario);
    }

    private UsuarioResponse convertirResponse(Usuario usuario) {

        Set<String> roles = usuario.getRoles()
                .stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.toSet());

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                roles
        );
    }
}