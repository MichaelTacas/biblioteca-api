package com.idat.biblioteca.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        // No aplicar JWT a rutas públicas
        return path.startsWith("/api/auth/")
                || path.startsWith("/swagger-ui/")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/v3/api-docs")
                || request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // Toda ruta que llega a este filtro es protegida.
        // Si no hay Bearer Token, responde 401.
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            enviarNoAutorizado(
                    response,
                    "Debe proporcionar un token de acceso"
            );

            return;
        }

        String token =
                authorizationHeader.substring(7).trim();

        if (token.isEmpty()) {

            enviarNoAutorizado(
                    response,
                    "Debe proporcionar un token de acceso"
            );

            return;
        }

        try {

            String email =
                    jwtService.obtenerUsername(token);

            if (email == null || email.isBlank()) {

                enviarNoAutorizado(
                        response,
                        "Token de acceso inválido"
                );

                return;
            }

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(email);

            // validarToken acepta únicamente ACCESS TOKEN
            if (!jwtService.validarToken(
                    token,
                    userDetails)) {

                enviarNoAutorizado(
                        response,
                        "Token de acceso inválido o no autorizado"
                );

                return;
            }

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();

            enviarNoAutorizado(
                    response,
                    "Token inválido o expirado"
            );
        }
    }

    private void enviarNoAutorizado(
            HttpServletResponse response,
            String mensaje)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
                "application/json;charset=UTF-8"
        );

        response.getWriter().write(
                """
                {
                  "estado": 401,
                  "error": "Unauthorized",
                  "mensaje": "%s"
                }
                """.formatted(mensaje)
        );
    }
}