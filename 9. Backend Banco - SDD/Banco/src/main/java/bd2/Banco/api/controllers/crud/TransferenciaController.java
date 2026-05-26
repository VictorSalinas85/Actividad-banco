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
@RequestMapping("/api/v1/transferencias")
@RequiredArgsConstructor
@Tag(name = "Transferencias", description = "CRUD de transferencias y sus aprobaciones")
@PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','SUPERVISOR_EMPRESA','EMPLEADO_EMPRESA_OPERATIVO')")
public class TransferenciaController {

    private final TrfTransferenciaCrudService transferenciaService;
    private final TrfTransferenciaAprobacionCrudService aprobacionService;

    // ── TrfTransferencia ───────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrfTransferenciaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(transferenciaService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrfTransferenciaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(transferenciaService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrfTransferenciaResponse>> crear(
            @Valid @RequestBody TrfTransferenciaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(transferenciaService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','SUPERVISOR_EMPRESA')")
    public ResponseEntity<ApiResponse<TrfTransferenciaResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody TrfTransferenciaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(transferenciaService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        transferenciaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Transferencia eliminada"));
    }

    // ── TrfTransferenciaAprobacion ─────────────────────────────────────────────

    @GetMapping("/aprobaciones")
    public ResponseEntity<ApiResponse<List<TrfTransferenciaAprobacionResponse>>> listarAprobaciones() {
        return ResponseEntity.ok(ApiResponse.ok(aprobacionService.buscarTodos()));
    }

    @GetMapping("/aprobaciones/{id}")
    public ResponseEntity<ApiResponse<TrfTransferenciaAprobacionResponse>> obtenerAprobacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(aprobacionService.buscarPorId(id)));
    }

    @PostMapping("/aprobaciones")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','SUPERVISOR_EMPRESA')")
    public ResponseEntity<ApiResponse<TrfTransferenciaAprobacionResponse>> crearAprobacion(
            @Valid @RequestBody TrfTransferenciaAprobacionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(aprobacionService.crear(request)));
    }

    @PutMapping("/aprobaciones/{id}")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','SUPERVISOR_EMPRESA')")
    public ResponseEntity<ApiResponse<TrfTransferenciaAprobacionResponse>> actualizarAprobacion(
            @PathVariable Long id, @Valid @RequestBody TrfTransferenciaAprobacionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(aprobacionService.actualizar(id, request)));
    }

    @DeleteMapping("/aprobaciones/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminarAprobacion(@PathVariable Long id) {
        aprobacionService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Aprobación de transferencia eliminada"));
    }
}
