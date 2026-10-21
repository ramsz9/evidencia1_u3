package com.evidencia1.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ResourceController {

    // Cualquier usuario autenticado
    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(Map.of(
                "username", jwt.getSubject(),
                "roles", jwt.getClaimAsString("scope")), HttpStatus.OK);
    }

    // Solo ADMIN
    @GetMapping("/admin")
    public ResponseEntity<?> admin() {
        return new ResponseEntity<>(Map.of("message", "Bienvenido al recurso de administración"), HttpStatus.OK);
    }

}
