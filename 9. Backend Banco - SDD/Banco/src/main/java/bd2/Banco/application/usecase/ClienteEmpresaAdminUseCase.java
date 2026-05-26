package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.AsignarRolEmpresaUsuarioRequest;
import bd2.Banco.domain.dto.request.AsociarUsuarioEmpresaRequest;
import bd2.Banco.domain.dto.request.ConsultarPendientesEmpresaRequest;
import bd2.Banco.domain.dto.request.SolicitarPrestamoRequest;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import bd2.Banco.domain.services.procedures.SpCliAsignarRolEmpresaUsuarioService;
import bd2.Banco.domain.services.procedures.SpCliAsociarUsuarioEmpresaService;
import bd2.Banco.domain.services.procedures.SpCreSolicitarPrestamoService;
import bd2.Banco.domain.services.procedures.SpTrfConsultarPendientesEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rol: CLIENTE_EMPRESA_ADMIN
 * Operaciones: administrar usuarios y roles de su empresa, consultar cuentas
 * y préstamos de la empresa, solicitar préstamos y ver transferencias pendientes.
 */
@Service
@RequiredArgsConstructor
public class ClienteEmpresaAdminUseCase {

    private final CliEmpresaCrudService empresaService;
    private final CliEmpresaUsuarioCrudService empresaUsuarioService;
    private final CliEmpresaUsuarioRolCrudService empresaUsuarioRolService;
    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;
    private final CrePrestamoCrudService prestamoService;
    private final TrfTransferenciaCrudService transferenciaService;
    private final SpCliAsociarUsuarioEmpresaService asociarUsuarioService;
    private final SpCliAsignarRolEmpresaUsuarioService asignarRolService;
    private final SpCreSolicitarPrestamoService solicitarPrestamoService;
    private final SpTrfConsultarPendientesEmpresaService consultarPendientesService;

    // -------------------------------------------------------------------------
    // Empresa y usuarios
    // -------------------------------------------------------------------------

    public CliEmpresaResponse verEmpresa(Long empresaId) {
        return empresaService.buscarPorId(empresaId);
    }

    public List<CliEmpresaUsuarioResponse> listarUsuariosEmpresa(Long empresaId) {
        return empresaUsuarioService.buscarTodos().stream()
                .filter(u -> empresaId.equals(u.getEmpresaId()))
                .collect(Collectors.toList());
    }

    public List<CliEmpresaUsuarioRolResponse> listarRolesUsuarioEmpresa(Long empresaUsuarioId) {
        return empresaUsuarioRolService.buscarTodos().stream()
                .filter(r -> empresaUsuarioId.equals(r.getEmpresaUsuarioId()))
                .collect(Collectors.toList());
    }

    public SpResultado asociarUsuario(AsociarUsuarioEmpresaRequest request) {
        return asociarUsuarioService.ejecutar(request);
    }

    public SpResultado asignarRol(AsignarRolEmpresaUsuarioRequest request) {
        return asignarRolService.ejecutar(request);
    }

    // -------------------------------------------------------------------------
    // Cuentas
    // -------------------------------------------------------------------------

    public CtaCuentaResponse verCuenta(Long cuentaId) {
        return cuentaService.buscarPorId(cuentaId);
    }

    public List<CtaCuentaResponse> listarCuentasEmpresa(Long empresaId) {
        return cuentaService.buscarTodos().stream()
                .filter(c -> empresaId.equals(c.getTitularEmpresaId()))
                .collect(Collectors.toList());
    }

    public List<CtaMovimientoResponse> listarMovimientosCuenta(Long cuentaId) {
        return movimientoService.buscarTodos().stream()
                .filter(m -> cuentaId.equals(m.getCuentaId()))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Préstamos
    // -------------------------------------------------------------------------

    public SpResultado solicitarPrestamo(SolicitarPrestamoRequest request) {
        return solicitarPrestamoService.ejecutar(request);
    }

    public CrePrestamoResponse verPrestamo(Long prestamoId) {
        return prestamoService.buscarPorId(prestamoId);
    }

    public List<CrePrestamoResponse> listarPrestamosEmpresa(Long empresaId) {
        return prestamoService.buscarTodos().stream()
                .filter(p -> empresaId.equals(p.getClienteEmpresaId()))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Transferencias
    // -------------------------------------------------------------------------

    public List<TransferenciaPendienteResponse> consultarTransferenciasPendientes(ConsultarPendientesEmpresaRequest request) {
        return consultarPendientesService.ejecutar(request);
    }

    public TrfTransferenciaResponse verTransferencia(Long transferenciaId) {
        return transferenciaService.buscarPorId(transferenciaId);
    }

    public List<TrfTransferenciaResponse> listarTransferenciasEmpresa(Long empresaId) {
        return transferenciaService.buscarTodos().stream()
                .filter(t -> empresaId.equals(t.getEmpresaId()))
                .collect(Collectors.toList());
    }
}
