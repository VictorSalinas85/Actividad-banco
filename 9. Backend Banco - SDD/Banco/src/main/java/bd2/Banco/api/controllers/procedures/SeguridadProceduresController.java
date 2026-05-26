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
@RequestMapping("/api/v1/ops/sesiones")
@RequiredArgsConstructor
@Tag(name = "Operaciones de Sesión", description = "Procedimientos almacenados para validación y revocación de sesiones")
public class SeguridadProceduresController {

    private final SpSecValidarSesionService validarSesionService;
    private final SpSecRevocarSesionService revocarSesionService;

    @PostMapping("/validar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Validar sesión activa", description = "Ejecuta sp_sec_validar_sesion")
    public ResponseEntity<ApiResponse<SpResultado>> validar(
            @Valid @RequestBody ValidarSesionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(validarSesionService.ejecutar(request)));
    }

    @PostMapping("/revocar")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Revocar sesión", description = "Ejecuta sp_sec_revocar_sesion")
    public ResponseEntity<ApiResponse<SpResultado>> revocar(
            @Valid @RequestBody RevocarSesionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(revocarSesionService.ejecutar(request)));
    }
}
