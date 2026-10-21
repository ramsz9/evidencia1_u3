package com.evidencia1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.evidencia1.dto.ErrorMessageDto;

@ControllerAdvice
public class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    // Login fallido en /api/auth/login
    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationExceptionHandler(AuthenticationException authenticationException) {
        log.warn("Login fallido: {}", authenticationException.getMessage());
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(
                "Usuario o contraseña incorrectos",
                HttpStatus.UNAUTHORIZED.toString());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.UNAUTHORIZED);
    }

}
