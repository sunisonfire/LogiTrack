package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.LoginRequest;
import com.DannaS2.LogiTrack.dto.response.LoginResponse;
import com.DannaS2.LogiTrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = "Operaciones para iniciar sesión y obtener el token de acceso"
)
public class AuthController {

    private final AuthService authService;

    // POST http://localhost:8080/auth/login
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida el usuario, la contraseña y el rol, y devuelve un token JWT para usar en las demás peticiones. Este endpoint es público."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "Usuario, contraseña o rol incorrectos")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
