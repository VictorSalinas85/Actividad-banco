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
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
@Tag(name = "Empresas", description = "CRUD de clientes empresa, usuarios de empresa y sus roles")
// Lectura: cualquier usuario autenticado (los formularios y lookups la necesitan).
// Escritura: restringida por método.
@PreAuthorize("isAuthenticated()")
public class ClienteEmpresaController {

    private final CliEmpresaCrudService empresaService;
    private final CliEmpresaUsuarioCrudService empresaUsuarioService;
    private final CliEmpresaUsuarioRolCrudService empresaUsuarioRolService;

    // ── CliEmpresa ─────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<CliEmpresaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(empresaService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CliEmpresaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(empresaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    public ResponseEntity<ApiResponse<CliEmpresaResponse>> crear(
            @Valid @RequestBody CliEmpresaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(empresaService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    public ResponseEntity<ApiResponse<CliEmpresaResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody CliEmpresaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(empresaService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        empresaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Empresa eliminada"));
    }

    // ── CliEmpresaUsuario ──────────────────────────────────────────────────────

    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<List<CliEmpresaUsuarioResponse>>> listarUsuarios() {
        return ResponseEntity.ok(ApiResponse.ok(empresaUsuarioService.buscarTodos()));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<ApiResponse<CliEmpresaUsuarioResponse>> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(empresaUsuarioService.buscarPorId(id)));
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    public ResponseEntity<ApiResponse<CliEmpresaUsuarioResponse>> crearUsuario(
            @Valid @RequestBody CliEmpresaUsuarioCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(empresaUsuarioService.crear(request)));
    }

    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    public ResponseEntity<ApiResponse<CliEmpresaUsuarioResponse>> actualizarUsuario(
            @PathVariable Long id, @Valid @RequestBody CliEmpresaUsuarioUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(empresaUsuarioService.actualizar(id, request)));
    }

    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
        empresaUsuarioService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Usuario de empresa eliminado"));
    }

    // ── CliEmpresaUsuarioRol ───────────────────────────────────────────────────

    @GetMapping("/usuarios/roles")
    public ResponseEntity<ApiResponse<List<CliEmpresaUsuarioRolResponse>>> listarRoles() {
        return ResponseEntity.ok(ApiResponse.ok(empresaUsuarioRolService.buscarTodos()));
    }

    @GetMapping("/usuarios/roles/{id}")
    public ResponseEntity<ApiResponse<CliEmpresaUsuarioRolResponse>> obtenerRol(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(empresaUsuarioRolService.buscarPorId(id)));
    }

    @PostMapping("/usuarios/roles")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL','CLIENTE_EMPRESA_ADMIN')")
    public ResponseEntity<ApiResponse<CliEmpresaUsuarioRolResponse>> asignarRol(
            @Valid @RequestBody CliEmpresaUsuarioRolCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(empresaUsuarioRolService.crear(request)));
    }

    @DeleteMapping("/usuarios/roles/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminarRol(@PathVariable Long id) {
        empresaUsuarioRolService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Rol de usuario empresa eliminado"));
    }
}
