package bd2.Banco.api.controllers.crud;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "CRUD de usuarios del sistema, roles y sesiones")
@PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
public class UsuarioController {

    private final SecUsuarioCrudService usuarioService;
    private final SecRolCrudService rolService;
    private final SecUsuarioRolCrudService usuarioRolService;
    private final SecSesionCrudService sesionService;

    // ── SecUsuario ─────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<SecUsuarioResponse>>> listarUsuarios() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SecUsuarioResponse>> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SecUsuarioResponse>> crearUsuario(
            @Valid @RequestBody SecUsuarioCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(usuarioService.crear(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SecUsuarioResponse>> actualizarUsuario(
            @PathVariable Long id, @Valid @RequestBody SecUsuarioUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Usuario eliminado"));
    }

    // ── SecRol ─────────────────────────────────────────────────────────────────

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<SecRolResponse>>> listarRoles() {
        return ResponseEntity.ok(ApiResponse.ok(rolService.buscarTodos()));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<SecRolResponse>> obtenerRol(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.buscarPorId(id)));
    }

    @PostMapping("/roles")
    public ResponseEntity<ApiResponse<SecRolResponse>> crearRol(
            @Valid @RequestBody SecRolCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(rolService.crear(request)));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<SecRolResponse>> actualizarRol(
            @PathVariable Long id, @Valid @RequestBody SecRolUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.actualizar(id, request)));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarRol(@PathVariable Long id) {
        rolService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Rol eliminado"));
    }

    // ── SecUsuarioRol ──────────────────────────────────────────────────────────

    @GetMapping("/asignaciones-rol")
    public ResponseEntity<ApiResponse<List<SecUsuarioRolResponse>>> listarAsignacionesRol() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioRolService.buscarTodos()));
    }

    @GetMapping("/asignaciones-rol/{id}")
    public ResponseEntity<ApiResponse<SecUsuarioRolResponse>> obtenerAsignacionRol(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioRolService.buscarPorId(id)));
    }

    @PostMapping("/asignaciones-rol")
    public ResponseEntity<ApiResponse<SecUsuarioRolResponse>> asignarRol(
            @Valid @RequestBody SecUsuarioRolCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(usuarioRolService.crear(request)));
    }

    @DeleteMapping("/asignaciones-rol/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarAsignacionRol(@PathVariable Long id) {
        usuarioRolService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Asignación de rol eliminada"));
    }

    // ── SecSesion ──────────────────────────────────────────────────────────────

    @GetMapping("/sesiones")
    public ResponseEntity<ApiResponse<List<SecSesionResponse>>> listarSesiones() {
        return ResponseEntity.ok(ApiResponse.ok(sesionService.buscarTodos()));
    }

    @GetMapping("/sesiones/{id}")
    public ResponseEntity<ApiResponse<SecSesionResponse>> obtenerSesion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(sesionService.buscarPorId(id)));
    }
}
