package bd2.Banco.api.controllers.auth;

import bd2.Banco.api.dto.auth.AuthUserResponse;
import bd2.Banco.api.dto.auth.LoginRequest;
import bd2.Banco.api.dto.auth.LoginResponse;
import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.security.CustomUserDetailsService;
import bd2.Banco.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login y consulta del usuario autenticado")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.expiration:86400}")
    private long jwtExpiration;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica usuario/email y retorna JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SecUsuario usuario = userDetailsService.loadSecUsuario(userDetails.getUsername());
        String token = jwtService.generateToken(userDetails, usuario.getId());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .expiresIn(jwtExpiration)
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Autenticación exitosa", response));
    }

    @GetMapping("/me")
    @Operation(summary = "Usuario autenticado", description = "Retorna información del usuario con el JWT actual")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(@AuthenticationPrincipal UserDetails userDetails) {
        SecUsuario usuario = userDetailsService.loadSecUsuario(userDetails.getUsername());

        AuthUserResponse response = AuthUserResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .estadoId(usuario.getEstado().getId())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .ultimoLoginAt(usuario.getUltimoLoginAt())
                .createdAt(usuario.getCreatedAt())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
