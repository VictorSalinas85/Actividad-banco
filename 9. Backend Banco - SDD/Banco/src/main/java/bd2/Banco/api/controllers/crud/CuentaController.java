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
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "CRUD de cuentas bancarias y movimientos")
@PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL','SUPERVISOR_EMPRESA')")
public class CuentaController {

    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;

    // ── CtaCuenta ──────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<CtaCuentaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(cuentaService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CtaCuentaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(cuentaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA')")
    public ResponseEntity<ApiResponse<CtaCuentaResponse>> crear(
            @Valid @RequestBody CtaCuentaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(cuentaService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA')")
    public ResponseEntity<ApiResponse<CtaCuentaResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody CtaCuentaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(cuentaService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        cuentaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Cuenta eliminada"));
    }

    // ── CtaMovimiento ──────────────────────────────────────────────────────────

    @GetMapping("/movimientos")
    public ResponseEntity<ApiResponse<List<CtaMovimientoResponse>>> listarMovimientos() {
        return ResponseEntity.ok(ApiResponse.ok(movimientoService.buscarTodos()));
    }

    @GetMapping("/movimientos/{id}")
    public ResponseEntity<ApiResponse<CtaMovimientoResponse>> obtenerMovimiento(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(movimientoService.buscarPorId(id)));
    }

    @PostMapping("/movimientos")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA')")
    public ResponseEntity<ApiResponse<CtaMovimientoResponse>> crearMovimiento(
            @Valid @RequestBody CtaMovimientoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(movimientoService.crear(request)));
    }

    @PutMapping("/movimientos/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<CtaMovimientoResponse>> actualizarMovimiento(
            @PathVariable Long id, @Valid @RequestBody CtaMovimientoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(movimientoService.actualizar(id, request)));
    }

    @DeleteMapping("/movimientos/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Movimiento eliminado"));
    }
}
