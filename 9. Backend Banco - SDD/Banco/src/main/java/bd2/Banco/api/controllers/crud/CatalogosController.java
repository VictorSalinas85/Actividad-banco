package bd2.Banco.api.controllers.crud;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos")
@RequiredArgsConstructor
@Tag(name = "Catálogos", description = "CRUD de tablas maestras del sistema")
@PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
public class CatalogosController {

    private final CatTipoIdentificacionCrudService tipoIdentificacionService;
    private final CatEstadoUsuarioCrudService estadoUsuarioService;
    private final CatTipoCuentaCrudService tipoCuentaService;
    private final CatEstadoCuentaCrudService estadoCuentaService;
    private final CatMonedaCrudService monedaService;
    private final CatCanalOperacionCrudService canalOperacionService;
    private final CatTipoMovimientoCrudService tipoMovimientoService;
    private final CatTipoOperacionCrudService tipoOperacionService;
    private final CatTipoPrestamoCrudService tipoPrestamoService;
    private final CatEstadoPrestamoCrudService estadoPrestamoService;
    private final CatMotivoRechazoCrudService motivoRechazoService;
    private final CatMotivoBloqueoCrudService motivoBloqueoService;
    private final CatEstadoTransferenciaCrudService estadoTransferenciaService;
    private final CatEstadoSesionCrudService estadoSesionService;
    private final CatParametroNegocioCrudService parametroNegocioService;
    private final CatRolEmpresaCrudService rolEmpresaService;
    private final CatTransicionEstadoCrudService transicionEstadoService;

    // ── CatTipoIdentificacion ──────────────────────────────────────────────────

    @GetMapping("/tipos-identificacion")
    public ResponseEntity<ApiResponse<List<CatTipoIdentificacionResponse>>> listarTiposIdentificacion() {
        return ResponseEntity.ok(ApiResponse.ok(tipoIdentificacionService.buscarTodos()));
    }

    @GetMapping("/tipos-identificacion/{id}")
    public ResponseEntity<ApiResponse<CatTipoIdentificacionResponse>> obtenerTipoIdentificacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(tipoIdentificacionService.buscarPorId(id)));
    }

    @PostMapping("/tipos-identificacion")
    @Operation(summary = "Crear tipo de identificación")
    public ResponseEntity<ApiResponse<CatTipoIdentificacionResponse>> crearTipoIdentificacion(
            @Valid @RequestBody CatTipoIdentificacionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(tipoIdentificacionService.crear(request)));
    }

    @PutMapping("/tipos-identificacion/{id}")
    public ResponseEntity<ApiResponse<CatTipoIdentificacionResponse>> actualizarTipoIdentificacion(
            @PathVariable Long id, @Valid @RequestBody CatTipoIdentificacionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(tipoIdentificacionService.actualizar(id, request)));
    }

    @DeleteMapping("/tipos-identificacion/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTipoIdentificacion(@PathVariable Long id) {
        tipoIdentificacionService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Tipo de identificación eliminado"));
    }

    // ── CatEstadoUsuario ───────────────────────────────────────────────────────

    @GetMapping("/estados-usuario")
    public ResponseEntity<ApiResponse<List<CatEstadoUsuarioResponse>>> listarEstadosUsuario() {
        return ResponseEntity.ok(ApiResponse.ok(estadoUsuarioService.buscarTodos()));
    }

    @GetMapping("/estados-usuario/{id}")
    public ResponseEntity<ApiResponse<CatEstadoUsuarioResponse>> obtenerEstadoUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(estadoUsuarioService.buscarPorId(id)));
    }

    @PostMapping("/estados-usuario")
    public ResponseEntity<ApiResponse<CatEstadoUsuarioResponse>> crearEstadoUsuario(
            @Valid @RequestBody CatEstadoUsuarioCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(estadoUsuarioService.crear(request)));
    }

    @PutMapping("/estados-usuario/{id}")
    public ResponseEntity<ApiResponse<CatEstadoUsuarioResponse>> actualizarEstadoUsuario(
            @PathVariable Long id, @Valid @RequestBody CatEstadoUsuarioUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(estadoUsuarioService.actualizar(id, request)));
    }

    @DeleteMapping("/estados-usuario/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEstadoUsuario(@PathVariable Long id) {
        estadoUsuarioService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Estado de usuario eliminado"));
    }

    // ── CatTipoCuenta ──────────────────────────────────────────────────────────

    @GetMapping("/tipos-cuenta")
    public ResponseEntity<ApiResponse<List<CatTipoCuentaResponse>>> listarTiposCuenta() {
        return ResponseEntity.ok(ApiResponse.ok(tipoCuentaService.buscarTodos()));
    }

    @GetMapping("/tipos-cuenta/{id}")
    public ResponseEntity<ApiResponse<CatTipoCuentaResponse>> obtenerTipoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(tipoCuentaService.buscarPorId(id)));
    }

    @PostMapping("/tipos-cuenta")
    public ResponseEntity<ApiResponse<CatTipoCuentaResponse>> crearTipoCuenta(
            @Valid @RequestBody CatTipoCuentaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(tipoCuentaService.crear(request)));
    }

    @PutMapping("/tipos-cuenta/{id}")
    public ResponseEntity<ApiResponse<CatTipoCuentaResponse>> actualizarTipoCuenta(
            @PathVariable Long id, @Valid @RequestBody CatTipoCuentaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(tipoCuentaService.actualizar(id, request)));
    }

    @DeleteMapping("/tipos-cuenta/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTipoCuenta(@PathVariable Long id) {
        tipoCuentaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Tipo de cuenta eliminado"));
    }

    // ── CatEstadoCuenta ────────────────────────────────────────────────────────

    @GetMapping("/estados-cuenta")
    public ResponseEntity<ApiResponse<List<CatEstadoCuentaResponse>>> listarEstadosCuenta() {
        return ResponseEntity.ok(ApiResponse.ok(estadoCuentaService.buscarTodos()));
    }

    @GetMapping("/estados-cuenta/{id}")
    public ResponseEntity<ApiResponse<CatEstadoCuentaResponse>> obtenerEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(estadoCuentaService.buscarPorId(id)));
    }

    @PostMapping("/estados-cuenta")
    public ResponseEntity<ApiResponse<CatEstadoCuentaResponse>> crearEstadoCuenta(
            @Valid @RequestBody CatEstadoCuentaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(estadoCuentaService.crear(request)));
    }

    @PutMapping("/estados-cuenta/{id}")
    public ResponseEntity<ApiResponse<CatEstadoCuentaResponse>> actualizarEstadoCuenta(
            @PathVariable Long id, @Valid @RequestBody CatEstadoCuentaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(estadoCuentaService.actualizar(id, request)));
    }

    @DeleteMapping("/estados-cuenta/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEstadoCuenta(@PathVariable Long id) {
        estadoCuentaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Estado de cuenta eliminado"));
    }

    // ── CatMoneda ──────────────────────────────────────────────────────────────

    @GetMapping("/monedas")
    public ResponseEntity<ApiResponse<List<CatMonedaResponse>>> listarMonedas() {
        return ResponseEntity.ok(ApiResponse.ok(monedaService.buscarTodos()));
    }

    @GetMapping("/monedas/{id}")
    public ResponseEntity<ApiResponse<CatMonedaResponse>> obtenerMoneda(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(monedaService.buscarPorId(id)));
    }

    @PostMapping("/monedas")
    public ResponseEntity<ApiResponse<CatMonedaResponse>> crearMoneda(
            @Valid @RequestBody CatMonedaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(monedaService.crear(request)));
    }

    @PutMapping("/monedas/{id}")
    public ResponseEntity<ApiResponse<CatMonedaResponse>> actualizarMoneda(
            @PathVariable Long id, @Valid @RequestBody CatMonedaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(monedaService.actualizar(id, request)));
    }

    @DeleteMapping("/monedas/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMoneda(@PathVariable Long id) {
        monedaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Moneda eliminada"));
    }

    // ── CatCanalOperacion ──────────────────────────────────────────────────────

    @GetMapping("/canales-operacion")
    public ResponseEntity<ApiResponse<List<CatCanalOperacionResponse>>> listarCanalesOperacion() {
        return ResponseEntity.ok(ApiResponse.ok(canalOperacionService.buscarTodos()));
    }

    @GetMapping("/canales-operacion/{id}")
    public ResponseEntity<ApiResponse<CatCanalOperacionResponse>> obtenerCanalOperacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(canalOperacionService.buscarPorId(id)));
    }

    @PostMapping("/canales-operacion")
    public ResponseEntity<ApiResponse<CatCanalOperacionResponse>> crearCanalOperacion(
            @Valid @RequestBody CatCanalOperacionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(canalOperacionService.crear(request)));
    }

    @PutMapping("/canales-operacion/{id}")
    public ResponseEntity<ApiResponse<CatCanalOperacionResponse>> actualizarCanalOperacion(
            @PathVariable Long id, @Valid @RequestBody CatCanalOperacionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(canalOperacionService.actualizar(id, request)));
    }

    @DeleteMapping("/canales-operacion/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCanalOperacion(@PathVariable Long id) {
        canalOperacionService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Canal de operación eliminado"));
    }

    // ── CatTipoMovimiento ──────────────────────────────────────────────────────

    @GetMapping("/tipos-movimiento")
    public ResponseEntity<ApiResponse<List<CatTipoMovimientoResponse>>> listarTiposMovimiento() {
        return ResponseEntity.ok(ApiResponse.ok(tipoMovimientoService.buscarTodos()));
    }

    @GetMapping("/tipos-movimiento/{id}")
    public ResponseEntity<ApiResponse<CatTipoMovimientoResponse>> obtenerTipoMovimiento(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(tipoMovimientoService.buscarPorId(id)));
    }

    @PostMapping("/tipos-movimiento")
    public ResponseEntity<ApiResponse<CatTipoMovimientoResponse>> crearTipoMovimiento(
            @Valid @RequestBody CatTipoMovimientoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(tipoMovimientoService.crear(request)));
    }

    @PutMapping("/tipos-movimiento/{id}")
    public ResponseEntity<ApiResponse<CatTipoMovimientoResponse>> actualizarTipoMovimiento(
            @PathVariable Long id, @Valid @RequestBody CatTipoMovimientoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(tipoMovimientoService.actualizar(id, request)));
    }

    @DeleteMapping("/tipos-movimiento/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTipoMovimiento(@PathVariable Long id) {
        tipoMovimientoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Tipo de movimiento eliminado"));
    }

    // ── CatTipoOperacion ───────────────────────────────────────────────────────

    @GetMapping("/tipos-operacion")
    public ResponseEntity<ApiResponse<List<CatTipoOperacionResponse>>> listarTiposOperacion() {
        return ResponseEntity.ok(ApiResponse.ok(tipoOperacionService.buscarTodos()));
    }

    @GetMapping("/tipos-operacion/{id}")
    public ResponseEntity<ApiResponse<CatTipoOperacionResponse>> obtenerTipoOperacion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(tipoOperacionService.buscarPorId(id)));
    }

    @PostMapping("/tipos-operacion")
    public ResponseEntity<ApiResponse<CatTipoOperacionResponse>> crearTipoOperacion(
            @Valid @RequestBody CatTipoOperacionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(tipoOperacionService.crear(request)));
    }

    @PutMapping("/tipos-operacion/{id}")
    public ResponseEntity<ApiResponse<CatTipoOperacionResponse>> actualizarTipoOperacion(
            @PathVariable Long id, @Valid @RequestBody CatTipoOperacionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(tipoOperacionService.actualizar(id, request)));
    }

    @DeleteMapping("/tipos-operacion/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTipoOperacion(@PathVariable Long id) {
        tipoOperacionService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Tipo de operación eliminado"));
    }

    // ── CatTipoPrestamo ────────────────────────────────────────────────────────

    @GetMapping("/tipos-prestamo")
    public ResponseEntity<ApiResponse<List<CatTipoPrestamoResponse>>> listarTiposPrestamo() {
        return ResponseEntity.ok(ApiResponse.ok(tipoPrestamoService.buscarTodos()));
    }

    @GetMapping("/tipos-prestamo/{id}")
    public ResponseEntity<ApiResponse<CatTipoPrestamoResponse>> obtenerTipoPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(tipoPrestamoService.buscarPorId(id)));
    }

    @PostMapping("/tipos-prestamo")
    public ResponseEntity<ApiResponse<CatTipoPrestamoResponse>> crearTipoPrestamo(
            @Valid @RequestBody CatTipoPrestamoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(tipoPrestamoService.crear(request)));
    }

    @PutMapping("/tipos-prestamo/{id}")
    public ResponseEntity<ApiResponse<CatTipoPrestamoResponse>> actualizarTipoPrestamo(
            @PathVariable Long id, @Valid @RequestBody CatTipoPrestamoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(tipoPrestamoService.actualizar(id, request)));
    }

    @DeleteMapping("/tipos-prestamo/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTipoPrestamo(@PathVariable Long id) {
        tipoPrestamoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Tipo de préstamo eliminado"));
    }

    // ── CatEstadoPrestamo ──────────────────────────────────────────────────────

    @GetMapping("/estados-prestamo")
    public ResponseEntity<ApiResponse<List<CatEstadoPrestamoResponse>>> listarEstadosPrestamo() {
        return ResponseEntity.ok(ApiResponse.ok(estadoPrestamoService.buscarTodos()));
    }

    @GetMapping("/estados-prestamo/{id}")
    public ResponseEntity<ApiResponse<CatEstadoPrestamoResponse>> obtenerEstadoPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(estadoPrestamoService.buscarPorId(id)));
    }

    @PostMapping("/estados-prestamo")
    public ResponseEntity<ApiResponse<CatEstadoPrestamoResponse>> crearEstadoPrestamo(
            @Valid @RequestBody CatEstadoPrestamoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(estadoPrestamoService.crear(request)));
    }

    @PutMapping("/estados-prestamo/{id}")
    public ResponseEntity<ApiResponse<CatEstadoPrestamoResponse>> actualizarEstadoPrestamo(
            @PathVariable Long id, @Valid @RequestBody CatEstadoPrestamoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(estadoPrestamoService.actualizar(id, request)));
    }

    @DeleteMapping("/estados-prestamo/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEstadoPrestamo(@PathVariable Long id) {
        estadoPrestamoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Estado de préstamo eliminado"));
    }

    // ── CatMotivoRechazo ───────────────────────────────────────────────────────

    @GetMapping("/motivos-rechazo")
    public ResponseEntity<ApiResponse<List<CatMotivoRechazoResponse>>> listarMotivosRechazo() {
        return ResponseEntity.ok(ApiResponse.ok(motivoRechazoService.buscarTodos()));
    }

    @GetMapping("/motivos-rechazo/{id}")
    public ResponseEntity<ApiResponse<CatMotivoRechazoResponse>> obtenerMotivoRechazo(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(motivoRechazoService.buscarPorId(id)));
    }

    @PostMapping("/motivos-rechazo")
    public ResponseEntity<ApiResponse<CatMotivoRechazoResponse>> crearMotivoRechazo(
            @Valid @RequestBody CatMotivoRechazoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(motivoRechazoService.crear(request)));
    }

    @PutMapping("/motivos-rechazo/{id}")
    public ResponseEntity<ApiResponse<CatMotivoRechazoResponse>> actualizarMotivoRechazo(
            @PathVariable Long id, @Valid @RequestBody CatMotivoRechazoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(motivoRechazoService.actualizar(id, request)));
    }

    @DeleteMapping("/motivos-rechazo/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMotivoRechazo(@PathVariable Long id) {
        motivoRechazoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Motivo de rechazo eliminado"));
    }

    // ── CatMotivoBloqueo ───────────────────────────────────────────────────────

    @GetMapping("/motivos-bloqueo")
    public ResponseEntity<ApiResponse<List<CatMotivoBloqueoResponse>>> listarMotivosBloqueo() {
        return ResponseEntity.ok(ApiResponse.ok(motivoBloqueoService.buscarTodos()));
    }

    @GetMapping("/motivos-bloqueo/{id}")
    public ResponseEntity<ApiResponse<CatMotivoBloqueoResponse>> obtenerMotivoBloqueo(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(motivoBloqueoService.buscarPorId(id)));
    }

    @PostMapping("/motivos-bloqueo")
    public ResponseEntity<ApiResponse<CatMotivoBloqueoResponse>> crearMotivoBloqueo(
            @Valid @RequestBody CatMotivoBloqueoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(motivoBloqueoService.crear(request)));
    }

    @PutMapping("/motivos-bloqueo/{id}")
    public ResponseEntity<ApiResponse<CatMotivoBloqueoResponse>> actualizarMotivoBloqueo(
            @PathVariable Long id, @Valid @RequestBody CatMotivoBloqueoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(motivoBloqueoService.actualizar(id, request)));
    }

    @DeleteMapping("/motivos-bloqueo/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMotivoBloqueo(@PathVariable Long id) {
        motivoBloqueoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Motivo de bloqueo eliminado"));
    }

    // ── CatEstadoTransferencia ─────────────────────────────────────────────────

    @GetMapping("/estados-transferencia")
    public ResponseEntity<ApiResponse<List<CatEstadoTransferenciaResponse>>> listarEstadosTransferencia() {
        return ResponseEntity.ok(ApiResponse.ok(estadoTransferenciaService.buscarTodos()));
    }

    @GetMapping("/estados-transferencia/{id}")
    public ResponseEntity<ApiResponse<CatEstadoTransferenciaResponse>> obtenerEstadoTransferencia(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(estadoTransferenciaService.buscarPorId(id)));
    }

    @PostMapping("/estados-transferencia")
    public ResponseEntity<ApiResponse<CatEstadoTransferenciaResponse>> crearEstadoTransferencia(
            @Valid @RequestBody CatEstadoTransferenciaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(estadoTransferenciaService.crear(request)));
    }

    @PutMapping("/estados-transferencia/{id}")
    public ResponseEntity<ApiResponse<CatEstadoTransferenciaResponse>> actualizarEstadoTransferencia(
            @PathVariable Long id, @Valid @RequestBody CatEstadoTransferenciaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(estadoTransferenciaService.actualizar(id, request)));
    }

    @DeleteMapping("/estados-transferencia/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEstadoTransferencia(@PathVariable Long id) {
        estadoTransferenciaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Estado de transferencia eliminado"));
    }

    // ── CatEstadoSesion ────────────────────────────────────────────────────────

    @GetMapping("/estados-sesion")
    public ResponseEntity<ApiResponse<List<CatEstadoSesionResponse>>> listarEstadosSesion() {
        return ResponseEntity.ok(ApiResponse.ok(estadoSesionService.buscarTodos()));
    }

    @GetMapping("/estados-sesion/{id}")
    public ResponseEntity<ApiResponse<CatEstadoSesionResponse>> obtenerEstadoSesion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(estadoSesionService.buscarPorId(id)));
    }

    @PostMapping("/estados-sesion")
    public ResponseEntity<ApiResponse<CatEstadoSesionResponse>> crearEstadoSesion(
            @Valid @RequestBody CatEstadoSesionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(estadoSesionService.crear(request)));
    }

    @PutMapping("/estados-sesion/{id}")
    public ResponseEntity<ApiResponse<CatEstadoSesionResponse>> actualizarEstadoSesion(
            @PathVariable Long id, @Valid @RequestBody CatEstadoSesionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(estadoSesionService.actualizar(id, request)));
    }

    @DeleteMapping("/estados-sesion/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEstadoSesion(@PathVariable Long id) {
        estadoSesionService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Estado de sesión eliminado"));
    }

    // ── CatParametroNegocio ────────────────────────────────────────────────────

    @GetMapping("/parametros-negocio")
    public ResponseEntity<ApiResponse<List<CatParametroNegocioResponse>>> listarParametrosNegocio() {
        return ResponseEntity.ok(ApiResponse.ok(parametroNegocioService.buscarTodos()));
    }

    @GetMapping("/parametros-negocio/{id}")
    public ResponseEntity<ApiResponse<CatParametroNegocioResponse>> obtenerParametroNegocio(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(parametroNegocioService.buscarPorId(id)));
    }

    @PostMapping("/parametros-negocio")
    public ResponseEntity<ApiResponse<CatParametroNegocioResponse>> crearParametroNegocio(
            @Valid @RequestBody CatParametroNegocioCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(parametroNegocioService.crear(request)));
    }

    @PutMapping("/parametros-negocio/{id}")
    public ResponseEntity<ApiResponse<CatParametroNegocioResponse>> actualizarParametroNegocio(
            @PathVariable Long id, @Valid @RequestBody CatParametroNegocioUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(parametroNegocioService.actualizar(id, request)));
    }

    @DeleteMapping("/parametros-negocio/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarParametroNegocio(@PathVariable Long id) {
        parametroNegocioService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Parámetro de negocio eliminado"));
    }

    // ── CatRolEmpresa ──────────────────────────────────────────────────────────

    @GetMapping("/roles-empresa")
    public ResponseEntity<ApiResponse<List<CatRolEmpresaResponse>>> listarRolesEmpresa() {
        return ResponseEntity.ok(ApiResponse.ok(rolEmpresaService.buscarTodos()));
    }

    @GetMapping("/roles-empresa/{id}")
    public ResponseEntity<ApiResponse<CatRolEmpresaResponse>> obtenerRolEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(rolEmpresaService.buscarPorId(id)));
    }

    @PostMapping("/roles-empresa")
    public ResponseEntity<ApiResponse<CatRolEmpresaResponse>> crearRolEmpresa(
            @Valid @RequestBody CatRolEmpresaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(rolEmpresaService.crear(request)));
    }

    @PutMapping("/roles-empresa/{id}")
    public ResponseEntity<ApiResponse<CatRolEmpresaResponse>> actualizarRolEmpresa(
            @PathVariable Long id, @Valid @RequestBody CatRolEmpresaUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(rolEmpresaService.actualizar(id, request)));
    }

    @DeleteMapping("/roles-empresa/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarRolEmpresa(@PathVariable Long id) {
        rolEmpresaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Rol de empresa eliminado"));
    }

    // ── CatTransicionEstado ────────────────────────────────────────────────────

    @GetMapping("/transiciones-estado")
    public ResponseEntity<ApiResponse<List<CatTransicionEstadoResponse>>> listarTransicionesEstado() {
        return ResponseEntity.ok(ApiResponse.ok(transicionEstadoService.buscarTodos()));
    }

    @GetMapping("/transiciones-estado/{id}")
    public ResponseEntity<ApiResponse<CatTransicionEstadoResponse>> obtenerTransicionEstado(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(transicionEstadoService.buscarPorId(id)));
    }

    @PostMapping("/transiciones-estado")
    public ResponseEntity<ApiResponse<CatTransicionEstadoResponse>> crearTransicionEstado(
            @Valid @RequestBody CatTransicionEstadoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(transicionEstadoService.crear(request)));
    }

    @PutMapping("/transiciones-estado/{id}")
    public ResponseEntity<ApiResponse<CatTransicionEstadoResponse>> actualizarTransicionEstado(
            @PathVariable Long id, @Valid @RequestBody CatTransicionEstadoUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(transicionEstadoService.actualizar(id, request)));
    }

    @DeleteMapping("/transiciones-estado/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTransicionEstado(@PathVariable Long id) {
        transicionEstadoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Transición de estado eliminada"));
    }
}
