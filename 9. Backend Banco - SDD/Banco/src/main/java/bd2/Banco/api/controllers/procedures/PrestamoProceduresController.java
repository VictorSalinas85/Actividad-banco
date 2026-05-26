package bd2.Banco.api.controllers.procedures;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.SpResultado;
import bd2.Banco.domain.services.procedures.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ops/prestamos")
@RequiredArgsConstructor
@Tag(name = "Operaciones de Préstamo", description = "Procedimientos almacenados para el ciclo de vida de créditos")
public class PrestamoProceduresController {

    private final SpCreSolicitarPrestamoService solicitarService;
    private final SpCreAprobarPrestamoService aprobarService;
    private final SpCreRechazarPrestamoService rechazarService;
    private final SpCreDesembolsarPrestamoService desembolsarService;

    @PostMapping("/solicitar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL','CLIENTE_PERSONA','CLIENTE_EMPRESA_ADMIN')")
    @Operation(summary = "Solicitar préstamo", description = "Ejecuta sp_cre_solicitar_prestamo")
    public ResponseEntity<ApiResponse<SpResultado>> solicitar(
            @Valid @RequestBody SolicitarPrestamoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(solicitarService.ejecutar(request)));
    }

    @PostMapping("/aprobar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Aprobar préstamo", description = "Ejecuta sp_cre_aprobar_prestamo")
    public ResponseEntity<ApiResponse<SpResultado>> aprobar(
            @Valid @RequestBody AprobarPrestamoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(aprobarService.ejecutar(request)));
    }

    @PostMapping("/rechazar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Rechazar préstamo", description = "Ejecuta sp_cre_rechazar_prestamo")
    public ResponseEntity<ApiResponse<SpResultado>> rechazar(
            @Valid @RequestBody RechazarPrestamoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(rechazarService.ejecutar(request)));
    }

    @PostMapping("/desembolsar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Desembolsar préstamo", description = "Ejecuta sp_cre_desembolsar_prestamo")
    public ResponseEntity<ApiResponse<SpResultado>> desembolsar(
            @Valid @RequestBody DesembolsarPrestamoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(desembolsarService.ejecutar(request)));
    }
}
