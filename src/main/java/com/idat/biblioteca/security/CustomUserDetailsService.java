package com.idat.biblioteca.security;

import com.idat.biblioteca.model.Usuario;
import com.idat.biblioteca.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        String[] roles = usuario.getRoles()
                .stream()
                .map(rol -> rol.getNombre().name())
                .toArray(String[]::new);

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(roles)
                .build();
    }
}