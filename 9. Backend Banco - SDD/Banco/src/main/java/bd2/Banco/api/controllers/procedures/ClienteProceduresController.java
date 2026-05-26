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
@RequestMapping("/api/v1/ops/clientes")
@RequiredArgsConstructor
@Tag(name = "Operaciones de Cliente", description = "Procedimientos almacenados para gestión de clientes")
public class ClienteProceduresController {

    private final SpCliCrearPersonaService crearPersonaService;
    private final SpCliCrearEmpresaService crearEmpresaService;
    private final SpCliCambiarEstadoClienteService cambiarEstadoService;
    private final SpCliAsociarUsuarioEmpresaService asociarUsuarioService;
    private final SpCliAsignarRolEmpresaUsuarioService asignarRolService;

    @PostMapping("/crear-persona")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Crear persona natural", description = "Ejecuta sp_cli_crear_persona")
    public ResponseEntity<ApiResponse<SpResultado>> crearPersona(
            @Valid @RequestBody CrearPersonaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(crearPersonaService.ejecutar(request)));
    }

    @PostMapping("/crear-empresa")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Crear empresa", description = "Ejecuta sp_cli_crear_empresa")
    public ResponseEntity<ApiResponse<SpResultado>> crearEmpresa(
            @Valid @RequestBody CrearEmpresaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(crearEmpresaService.ejecutar(request)));
    }

    @PostMapping("/cambiar-estado")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Cambiar estado de cliente", description = "Ejecuta sp_cli_cambiar_estado_cliente")
    public ResponseEntity<ApiResponse<SpResultado>> cambiarEstado(
            @Valid @RequestBody CambiarEstadoClienteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(cambiarEstadoService.ejecutar(request)));
    }

    @PostMapping("/asociar-usuario")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_COMERCIAL')")
    @Operation(summary = "Asociar usuario a empresa", description = "Ejecuta sp_cli_asociar_usuario_empresa")
    public ResponseEntity<ApiResponse<SpResultado>> asociarUsuario(
            @Valid @RequestBody AsociarUsuarioEmpresaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(asociarUsuarioService.ejecutar(request)));
    }

    @PostMapping("/asignar-rol")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','CLIENTE_EMPRESA_ADMIN')")
    @Operation(summary = "Asignar rol empresa a usuario", description = "Ejecuta sp_cli_asignar_rol_empresa_usuario")
    public ResponseEntity<ApiResponse<SpResultado>> asignarRol(
            @Valid @RequestBody AsignarRolEmpresaUsuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(asignarRolService.ejecutar(request)));
    }
}
