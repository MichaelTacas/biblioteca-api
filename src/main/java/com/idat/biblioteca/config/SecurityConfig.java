package com.idat.biblioteca.config;

import com.idat.biblioteca.security.CustomUserDetailsService;
import com.idat.biblioteca.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        return new ProviderManager(
                authenticationProvider()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // API REST sin CSRF
                .csrf(csrf -> csrf.disable())

                // JWT = sin sesiones
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

// Importante: sin JWT no crear ROLE_ANONYMOUS
                .anonymous(anonymous -> anonymous.disable())

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );

                                    response.getWriter().write("""
                            {
                              "estado": 401,
                              "error": "Unauthorized",
                              "mensaje": "Debe autenticarse para acceder a este recurso"
                            }
                            """);
                                }
                        )

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );

                                    response.getWriter().write("""
                            {
                              "estado": 403,
                              "error": "Forbidden",
                              "mensaje": "No tiene permisos para realizar esta operación"
                            }
                            """);
                                }
                        )
                )

                // Respuestas personalizadas 401 / 403
                .exceptionHandling(exception -> exception

                        // No autenticado
                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );

                                    response.getWriter().write("""
                                            {
                                              "estado": 401,
                                              "error": "Unauthorized",
                                              "mensaje": "Debe autenticarse para acceder a este recurso"
                                            }
                                            """);
                                }
                        )

                        // Autenticado pero sin permisos
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json;charset=UTF-8"
                                    );

                                    response.getWriter().write("""
                                            {
                                              "estado": 403,
                                              "error": "Forbidden",
                                              "mensaje": "No tiene permisos para realizar esta operación"
                                            }
                                            """);
                                }
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // RUTAS PÚBLICAS
                        // =========================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // =========================
                        // LIBROS
                        // =========================

                        // ADMIN y USUARIO pueden consultar
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/libros/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "USUARIO"
                        )

                        // Solo ADMIN registra
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/libros/**"
                        ).hasRole("ADMIN")

                        // Solo ADMIN actualiza
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/libros/**"
                        ).hasRole("ADMIN")

                        // Solo ADMIN elimina
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/libros/**"
                        ).hasRole("ADMIN")

                        // =========================
                        // USUARIOS
                        // =========================

                        .requestMatchers(
                                "/api/usuarios/**"
                        ).hasRole("ADMIN")

                        // =========================
                        // PRÉSTAMOS
                        // =========================

                        // USUARIO puede consultar
                        // únicamente su propio historial
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/prestamos/mis-prestamos"
                        ).hasAnyRole(
                                "ADMIN",
                                "USUARIO"
                        )

                        // Solo ADMIN registra préstamos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/prestamos"
                        ).hasRole("ADMIN")

                        // Solo ADMIN registra devoluciones
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/prestamos/*/devolucion"
                        ).hasRole("ADMIN")

                        // Demás consultas de préstamos:
                        // solo ADMIN
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/prestamos/**"
                        ).hasRole("ADMIN")

                        // Cualquier otra ruta necesita autenticación
                        .anyRequest()
                        .authenticated()
                )

                .authenticationProvider(
                        authenticationProvider()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}