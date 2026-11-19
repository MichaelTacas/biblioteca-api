package com.idat.biblioteca.config;

import com.idat.biblioteca.model.NombreRol;
import com.idat.biblioteca.model.Rol;
import com.idat.biblioteca.model.Usuario;
import com.idat.biblioteca.repository.RolRepository;
import com.idat.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.nombre}")
    private String adminNombre;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataInitializer(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Crear ROLE_USUARIO si no existe
        rolRepository.findByNombre(NombreRol.ROLE_USUARIO)
                .orElseGet(() ->
                        rolRepository.save(
                                new Rol(NombreRol.ROLE_USUARIO)
                        )
                );

        // Crear ROLE_ADMIN si no existe
        Rol rolAdmin = rolRepository
                .findByNombre(NombreRol.ROLE_ADMIN)
                .orElseGet(() ->
                        rolRepository.save(
                                new Rol(NombreRol.ROLE_ADMIN)
                        )
                );

        // Crear usuario administrador si no existe
        if (!usuarioRepository.existsByEmail(adminEmail)) {

            Usuario admin = new Usuario();

            admin.setNombre(adminNombre);
            admin.setEmail(adminEmail);
            admin.setPassword(
                    passwordEncoder.encode(adminPassword)
            );

            admin.getRoles().add(rolAdmin);

            usuarioRepository.save(admin);

            System.out.println(
                    "Usuario administrador inicial creado correctamente."
            );
        }
    }
}