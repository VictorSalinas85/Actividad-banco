package bd2.Banco.api.controllers.procedures;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.services.procedures.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ops/auditoria")
@RequiredArgsConstructor
@Tag(name = "Operaciones de Auditoría", description = "Procedimientos almacenados para registro de eventos y errores")
@PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
public class AuditoriaProceduresController {

    private final SpAudRegistrarEventoService registrarEventoService;
    private final SpAudRegistrarErrorService registrarErrorService;

    @PostMapping("/registrar-evento")
    @Operation(summary = "Registrar evento de auditoría", description = "Ejecuta sp_aud_registrar_evento")
    public ResponseEntity<ApiResponse<Void>> registrarEvento(
            @Valid @RequestBody RegistrarEventoRequest request) {
        registrarEventoService.ejecutar(request);
        return ResponseEntity.ok(ApiResponse.noContent("Evento registrado"));
    }

    @PostMapping("/registrar-error")
    @Operation(summary = "Registrar error de operación", description = "Ejecuta sp_aud_registrar_error")
    public ResponseEntity<ApiResponse<Void>> registrarError(
            @Valid @RequestBody RegistrarErrorRequest request) {
        registrarErrorService.ejecutar(request);
        return ResponseEntity.ok(ApiResponse.noContent("Error registrado"));
    }
}
