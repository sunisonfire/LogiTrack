
package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.LoginRequest;
import com.DannaS2.LogiTrack.dto.request.RegistroRequest;
import com.DannaS2.LogiTrack.dto.response.LoginResponse;
import com.DannaS2.LogiTrack.dto.response.RegistroResponse;
import com.DannaS2.LogiTrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = "Operaciones para registro e inicio de sesión"
)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales y devuelve un token JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Solicitar registro de empleado",
            description = "Registra una solicitud pendiente de aprobación. No crea una cuenta activa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Documento o usuario duplicado")
    })
    public ResponseEntity<RegistroResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.registrar(request));
    }
}
