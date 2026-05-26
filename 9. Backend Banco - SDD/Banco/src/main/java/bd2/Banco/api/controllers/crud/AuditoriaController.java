package bd2.Banco.api.controllers.crud;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoría", description = "Consulta de bitácoras, cambios de datos y errores de operación (solo lectura)")
@PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
public class AuditoriaController {

    private final AudBitacoraEventoCrudService bitacoraService;
    private final AudCambioDatoCrudService cambioDatoService;
    private final AudErrorOperacionCrudService errorOperacionService;

    // ── AudBitacoraEvento ──────────────────────────────────────────────────────

    @GetMapping("/bitacora")
    public ResponseEntity<ApiResponse<List<AudBitacoraEventoResponse>>> listarBitacora() {
        return ResponseEntity.ok(ApiResponse.ok(bitacoraService.buscarTodos()));
    }

    @GetMapping("/bitacora/{id}")
    public ResponseEntity<ApiResponse<AudBitacoraEventoResponse>> obtenerBitacora(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(bitacoraService.buscarPorId(id)));
    }

    // ── AudCambioDato ──────────────────────────────────────────────────────────

    @GetMapping("/cambios")
    public ResponseEntity<ApiResponse<List<AudCambioDatoResponse>>> listarCambios() {
        return ResponseEntity.ok(ApiResponse.ok(cambioDatoService.buscarTodos()));
    }

    @GetMapping("/cambios/{id}")
    public ResponseEntity<ApiResponse<AudCambioDatoResponse>> obtenerCambio(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(cambioDatoService.buscarPorId(id)));
    }

    // ── AudErrorOperacion ──────────────────────────────────────────────────────

    @GetMapping("/errores")
    public ResponseEntity<ApiResponse<List<AudErrorOperacionResponse>>> listarErrores() {
        return ResponseEntity.ok(ApiResponse.ok(errorOperacionService.buscarTodos()));
    }

    @GetMapping("/errores/{id}")
    public ResponseEntity<ApiResponse<AudErrorOperacionResponse>> obtenerError(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(errorOperacionService.buscarPorId(id)));
    }
}
