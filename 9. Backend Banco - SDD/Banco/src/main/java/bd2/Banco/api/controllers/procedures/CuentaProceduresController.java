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
@RequestMapping("/api/v1/ops/cuentas")
@RequiredArgsConstructor
@Tag(name = "Operaciones de Cuenta", description = "Procedimientos almacenados para gestión de cuentas bancarias")
public class CuentaProceduresController {

    private final SpCtaAbrirCuentaService abrirCuentaService;
    private final SpCtaBloquearCuentaService bloquearCuentaService;
    private final SpCtaCancelarCuentaService cancelarCuentaService;
    private final SpCtaConsignarService consignarService;
    private final SpCtaRetirarService retirarService;

    @PostMapping("/abrir")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Abrir cuenta bancaria", description = "Ejecuta sp_cta_abrir_cuenta")
    public ResponseEntity<ApiResponse<SpResultado>> abrirCuenta(
            @Valid @RequestBody AbrirCuentaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(abrirCuentaService.ejecutar(request)));
    }

    @PostMapping("/bloquear")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA')")
    @Operation(summary = "Bloquear cuenta bancaria", description = "Ejecuta sp_cta_bloquear_cuenta")
    public ResponseEntity<ApiResponse<SpResultado>> bloquearCuenta(
            @Valid @RequestBody BloquearCuentaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(bloquearCuentaService.ejecutar(request)));
    }

    @PostMapping("/cancelar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA')")
    @Operation(summary = "Cancelar cuenta bancaria", description = "Ejecuta sp_cta_cancelar_cuenta")
    public ResponseEntity<ApiResponse<SpResultado>> cancelarCuenta(
            @Valid @RequestBody CancelarCuentaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(cancelarCuentaService.ejecutar(request)));
    }

    @PostMapping("/consignar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','CLIENTE_PERSONA','CLIENTE_EMPRESA_ADMIN','EMPLEADO_EMPRESA_OPERATIVO')")
    @Operation(summary = "Consignar en cuenta", description = "Ejecuta sp_cta_consignar")
    public ResponseEntity<ApiResponse<SpResultado>> consignar(
            @Valid @RequestBody ConsignarRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(consignarService.ejecutar(request)));
    }

    @PostMapping("/retirar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','CLIENTE_PERSONA','CLIENTE_EMPRESA_ADMIN','EMPLEADO_EMPRESA_OPERATIVO')")
    @Operation(summary = "Retirar de cuenta", description = "Ejecuta sp_cta_retirar")
    public ResponseEntity<ApiResponse<SpResultado>> retirar(
            @Valid @RequestBody RetirarRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(retirarService.ejecutar(request)));
    }
}
