package com.idat.biblioteca.service.impl;

import com.idat.biblioteca.dto.request.LoginRequest;
import com.idat.biblioteca.dto.request.RegistroUsuarioRequest;
import com.idat.biblioteca.dto.response.AuthResponse;
import com.idat.biblioteca.dto.response.UsuarioResponse;
import com.idat.biblioteca.exception.ReglaNegocioException;
import com.idat.biblioteca.model.NombreRol;
import com.idat.biblioteca.model.Rol;
import com.idat.biblioteca.model.Usuario;
import com.idat.biblioteca.repository.RolRepository;
import com.idat.biblioteca.repository.UsuarioRepository;
import com.idat.biblioteca.security.CustomUserDetailsService;
import com.idat.biblioteca.security.JwtService;
import com.idat.biblioteca.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idat.biblioteca.dto.request.RefreshTokenRequest;
import io.jsonwebtoken.JwtException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UsuarioResponse registrar(RegistroUsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ReglaNegocioException(
                    "El email ya se encuentra registrado"
            );
        }

        Rol rolUsuario = rolRepository
                .findByNombre(NombreRol.ROLE_USUARIO)
                .orElseGet(() ->
                        rolRepository.save(
                                new Rol(NombreRol.ROLE_USUARIO)
                        )
                );

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());

        usuario.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        usuario.getRoles().add(rolUsuario);

        Usuario guardado = usuarioRepository.save(usuario);

        return convertirResponse(guardado);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        String accessToken =
                jwtService.generarAccessToken(userDetails);

        String refreshToken =
                jwtService.generarRefreshToken(userDetails);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                request.getEmail()
        );
    }

    @Override
    public AuthResponse refrescarToken(
            RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        try {

            String email =
                    jwtService.obtenerUsername(refreshToken);

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            if (!jwtService.validarRefreshToken(
                    refreshToken,
                    userDetails)) {

                throw new ReglaNegocioException(
                        "Refresh token inválido o expirado"
                );
            }

            String nuevoAccessToken =
                    jwtService.generarAccessToken(userDetails);

            String nuevoRefreshToken =
                    jwtService.generarRefreshToken(userDetails);

            return new AuthResponse(
                    nuevoAccessToken,
                    nuevoRefreshToken,
                    "Bearer",
                    email
            );

        } catch (JwtException | IllegalArgumentException ex) {

            throw new ReglaNegocioException(
                    "Refresh token inválido o expirado"
            );
        }
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