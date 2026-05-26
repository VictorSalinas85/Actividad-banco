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
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
@Tag(name = "Préstamos", description = "CRUD de solicitudes de crédito, aprobaciones y desembolsos")
@PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
public class PrestamoController {

    private final CrePrestamoCrudService prestamoService;
    private final CrePrestamoAprobacionCrudService aprobacionService;
    private final CrePrestamoDesembolsoCrudService desembolsoService;

    // ── CrePrestamo ────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<CrePrestamoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(prestamoService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CrePrestamoResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(prestamoService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CrePrestamoResponse>> crear(
            @Valid @RequestBody CrePrestamoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(prestamoService.crear(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CrePrestamoResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody CrePrestamoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(prestamoService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        prestamoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Préstamo eliminado"));
    }

    // ── CrePrestamoAprobacion ──────────────────────────────────────────────────

    @GetMapping("/aprobaciones")
    public ResponseEntity<ApiResponse<List<CrePrestamoAprobacionResponse>>> listarAprobaciones() {
        return ResponseEntity.ok(ApiResponse.ok(aprobacionService.buscarTodos()));
    }

    @GetMapping("/aprobaciones/{id}")
    public ResponseEntity<ApiResponse<CrePrestamoAprobacionResponse>> obtenerAprobacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(aprobacionService.buscarPorId(id)));
    }

    @PostMapping("/aprobaciones")
    public ResponseEntity<ApiResponse<CrePrestamoAprobacionResponse>> crearAprobacion(
            @Valid @RequestBody CrePrestamoAprobacionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(aprobacionService.crear(request)));
    }

    @PutMapping("/aprobaciones/{id}")
    public ResponseEntity<ApiResponse<CrePrestamoAprobacionResponse>> actualizarAprobacion(
            @PathVariable Long id, @Valid @RequestBody CrePrestamoAprobacionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(aprobacionService.actualizar(id, request)));
    }

    @DeleteMapping("/aprobaciones/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminarAprobacion(@PathVariable Long id) {
        aprobacionService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Aprobación eliminada"));
    }

    // ── CrePrestamoDesembolso ──────────────────────────────────────────────────

    @GetMapping("/desembolsos")
    public ResponseEntity<ApiResponse<List<CrePrestamoDesembolsoResponse>>> listarDesembolsos() {
        return ResponseEntity.ok(ApiResponse.ok(desembolsoService.buscarTodos()));
    }

    @GetMapping("/desembolsos/{id}")
    public ResponseEntity<ApiResponse<CrePrestamoDesembolsoResponse>> obtenerDesembolso(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(desembolsoService.buscarPorId(id)));
    }

    @PostMapping("/desembolsos")
    public ResponseEntity<ApiResponse<CrePrestamoDesembolsoResponse>> crearDesembolso(
            @Valid @RequestBody CrePrestamoDesembolsoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(desembolsoService.crear(request)));
    }

    @PutMapping("/desembolsos/{id}")
    public ResponseEntity<ApiResponse<CrePrestamoDesembolsoResponse>> actualizarDesembolso(
            @PathVariable Long id, @Valid @RequestBody CrePrestamoDesembolsoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(desembolsoService.actualizar(id, request)));
    }

    @DeleteMapping("/desembolsos/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminarDesembolso(@PathVariable Long id) {
        desembolsoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Desembolso eliminado"));
    }
}
