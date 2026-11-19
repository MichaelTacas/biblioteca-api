package com.idat.biblioteca.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String CLAIM_TIPO = "tipo";

    private static final String TIPO_ACCESS = "ACCESS";
    private static final String TIPO_REFRESH = "REFRESH";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String generarAccessToken(
            UserDetails userDetails) {

        return construirToken(
                userDetails.getUsername(),
                expiration,
                TIPO_ACCESS
        );
    }

    public String generarRefreshToken(
            UserDetails userDetails) {

        return construirToken(
                userDetails.getUsername(),
                refreshExpiration,
                TIPO_REFRESH
        );
    }

    // Compatibilidad con el código anterior
    public String generarToken(
            UserDetails userDetails) {

        return generarAccessToken(userDetails);
    }

    public String obtenerUsername(String token) {

        return obtenerClaims(token)
                .getSubject();
    }

    public boolean validarAccessToken(
            String token,
            UserDetails userDetails) {

        String username = obtenerUsername(token);

        return username.equals(
                userDetails.getUsername()
        )
                && !estaExpirado(token)
                && TIPO_ACCESS.equals(
                obtenerTipoToken(token)
        );
    }

    public boolean validarRefreshToken(
            String token,
            UserDetails userDetails) {

        String username = obtenerUsername(token);

        return username.equals(
                userDetails.getUsername()
        )
                && !estaExpirado(token)
                && TIPO_REFRESH.equals(
                obtenerTipoToken(token)
        );
    }

    // Compatibilidad con JwtAuthenticationFilter actual
    public boolean validarToken(
            String token,
            UserDetails userDetails) {

        return validarAccessToken(
                token,
                userDetails
        );
    }

    private String construirToken(
            String username,
            long duracion,
            String tipo) {

        Date ahora = new Date();

        Date fechaExpiracion =
                new Date(
                        ahora.getTime() + duracion
                );

        return Jwts.builder()
                .subject(username)
                .claim(CLAIM_TIPO, tipo)
                .issuedAt(ahora)
                .expiration(fechaExpiracion)
                .signWith(obtenerClave())
                .compact();
    }

    private String obtenerTipoToken(
            String token) {

        Object tipo = obtenerClaims(token)
                .get(CLAIM_TIPO);

        return tipo != null
                ? tipo.toString()
                : null;
    }

    private boolean estaExpirado(
            String token) {

        return obtenerClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims obtenerClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClave() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}