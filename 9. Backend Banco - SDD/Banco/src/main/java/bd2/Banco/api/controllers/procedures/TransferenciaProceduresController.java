package bd2.Banco.api.controllers.procedures;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.SpResultado;
import bd2.Banco.domain.dto.response.TransferenciaPendienteResponse;
import bd2.Banco.domain.services.procedures.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ops/transferencias")
@RequiredArgsConstructor
@Tag(name = "Operaciones de Transferencia", description = "Procedimientos almacenados para el ciclo de vida de transferencias")
public class TransferenciaProceduresController {

    private final SpTrfCrearTransferenciaService crearService;
    private final SpTrfAprobarTransferenciaService aprobarService;
    private final SpTrfRechazarTransferenciaService rechazarService;
    private final SpTrfEjecutarTransferenciaDirectaService ejecutarDirectaService;
    private final SpTrfVencerTransferenciasPendientesService vencerService;
    private final SpTrfConsultarPendientesEmpresaService consultarPendientesService;

    @PostMapping("/crear")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','CLIENTE_PERSONA','CLIENTE_EMPRESA_ADMIN','EMPLEADO_EMPRESA_OPERATIVO')")
    @Operation(summary = "Crear transferencia", description = "Ejecuta sp_trf_crear_transferencia")
    public ResponseEntity<ApiResponse<SpResultado>> crear(
            @Valid @RequestBody CrearTransferenciaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(crearService.ejecutar(request)));
    }

    @PostMapping("/aprobar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','SUPERVISOR_EMPRESA')")
    @Operation(summary = "Aprobar transferencia pendiente", description = "Ejecuta sp_trf_aprobar_transferencia")
    public ResponseEntity<ApiResponse<SpResultado>> aprobar(
            @Valid @RequestBody AprobarTransferenciaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(aprobarService.ejecutar(request)));
    }

    @PostMapping("/rechazar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','SUPERVISOR_EMPRESA')")
    @Operation(summary = "Rechazar transferencia pendiente", description = "Ejecuta sp_trf_rechazar_transferencia")
    public ResponseEntity<ApiResponse<SpResultado>> rechazar(
            @Valid @RequestBody RechazarTransferenciaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(rechazarService.ejecutar(request)));
    }

    @PostMapping("/ejecutar-directa")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','CLIENTE_PERSONA')")
    @Operation(summary = "Ejecutar transferencia directa", description = "Ejecuta sp_trf_ejecutar_transferencia_directa")
    public ResponseEntity<ApiResponse<SpResultado>> ejecutarDirecta(
            @Valid @RequestBody EjecutarTransferenciaDirectaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(ejecutarDirectaService.ejecutar(request)));
    }

    @PostMapping("/vencer-pendientes")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    @Operation(summary = "Vencer transferencias pendientes", description = "Ejecuta sp_trf_vencer_transferencias_pendientes")
    public ResponseEntity<ApiResponse<SpResultado>> vencerPendientes(
            @Valid @RequestBody VencerTransferenciasPendientesRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(vencerService.ejecutar(request)));
    }

    @PostMapping("/pendientes-empresa")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','SUPERVISOR_EMPRESA','CLIENTE_EMPRESA_ADMIN')")
    @Operation(summary = "Consultar transferencias pendientes de empresa", description = "Ejecuta sp_trf_consultar_pendientes_aprobacion_empresa")
    public ResponseEntity<ApiResponse<List<TransferenciaPendienteResponse>>> pendientesEmpresa(
            @Valid @RequestBody ConsultarPendientesEmpresaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(consultarPendientesService.ejecutar(request)));
    }
}
