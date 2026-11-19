package com.idat.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import java.util.HashMap;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException exception) {

        Map<String, Object> respuesta = crearRespuesta(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio(
            ReglaNegocioException exception) {

        Map<String, Object> respuesta = crearRespuesta(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(
            AccesoDenegadoException exception) {

        Map<String, Object> respuesta = crearRespuesta(
                HttpStatus.FORBIDDEN,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(respuesta);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> manejarAccessDenied(
            AccessDeniedException exception) {

        Map<String, Object> respuesta = crearRespuesta(
                HttpStatus.FORBIDDEN,
                "No tiene permisos para realizar esta operación"
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(
            MethodArgumentNotValidException exception) {

        Map<String, String> errores = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errores);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarIntegridadDatos(
            DataIntegrityViolationException ex) {

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", HttpStatus.CONFLICT.value());
        respuesta.put("error", "Conflict");
        respuesta.put(
                "mensaje",
                "La operación no puede realizarse porque existen datos relacionados o duplicados"
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }

    private Map<String, Object> crearRespuesta(
            HttpStatus status,
            String mensaje) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);

        return respuesta;
    }
}