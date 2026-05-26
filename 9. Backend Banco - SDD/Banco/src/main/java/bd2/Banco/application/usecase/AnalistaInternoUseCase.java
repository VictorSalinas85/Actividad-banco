package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import bd2.Banco.domain.services.procedures.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Rol: ANALISTA_INTERNO
 * Operaciones: gestión integral de clientes, cuentas, préstamos,
 * transferencias y acceso completo a auditoría.
 */
@Service
@RequiredArgsConstructor
public class AnalistaInternoUseCase {

    private final CliPersonaNaturalCrudService personaService;
    private final CliEmpresaCrudService empresaService;
    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;
    private final CrePrestamoCrudService prestamoService;
    private final CrePrestamoAprobacionCrudService aprobacionPrestamoService;
    private final TrfTransferenciaCrudService transferenciaService;
    private final AudBitacoraEventoCrudService bitacoraService;
    private final AudCambioDatoCrudService cambioDatoService;
    private final AudErrorOperacionCrudService errorService;
    private final SpCliCambiarEstadoClienteService cambiarEstadoClienteService;
    private final SpCtaBloquearCuentaService bloquearCuentaService;
    private final SpCtaCancelarCuentaService cancelarCuentaService;
    private final SpCreAprobarPrestamoService aprobarPrestamoService;
    private final SpCreRechazarPrestamoService rechazarPrestamoService;
    private final SpCreDesembolsarPrestamoService desembolsarPrestamoService;
    private final SpTrfVencerTransferenciasPendientesService vencerPendientesService;
    private final SpAudRegistrarEventoService registrarEventoService;

    // -------------------------------------------------------------------------
    // Clientes
    // -------------------------------------------------------------------------

    public List<CliPersonaNaturalResponse> listarPersonas() {
        return personaService.buscarTodos();
    }

    public CliPersonaNaturalResponse verPersona(Long personaId) {
        return personaService.buscarPorId(personaId);
    }

    public List<CliEmpresaResponse> listarEmpresas() {
        return empresaService.buscarTodos();
    }

    public CliEmpresaResponse verEmpresa(Long empresaId) {
        return empresaService.buscarPorId(empresaId);
    }

    public SpResultado cambiarEstadoCliente(CambiarEstadoClienteRequest request) {
        return cambiarEstadoClienteService.ejecutar(request);
    }

    // -------------------------------------------------------------------------
    // Cuentas
    // -------------------------------------------------------------------------

    public List<CtaCuentaResponse> listarCuentas() {
        return cuentaService.buscarTodos();
    }

    public CtaCuentaResponse verCuenta(Long cuentaId) {
        return cuentaService.buscarPorId(cuentaId);
    }

    public List<CtaMovimientoResponse> listarMovimientosCuenta(Long cuentaId) {
        return movimientoService.buscarTodos().stream()
                .filter(m -> cuentaId.equals(m.getCuentaId()))
                .collect(java.util.stream.Collectors.toList());
    }

    public SpResultado bloquearCuenta(BloquearCuentaRequest request) {
        return bloquearCuentaService.ejecutar(request);
    }

    public SpResultado cancelarCuenta(CancelarCuentaRequest request) {
        return cancelarCuentaService.ejecutar(request);
    }

    // -------------------------------------------------------------------------
    // Préstamos
    // -------------------------------------------------------------------------

    public List<CrePrestamoResponse> listarPrestamos() {
        return prestamoService.buscarTodos();
    }

    public CrePrestamoResponse verPrestamo(Long prestamoId) {
        return prestamoService.buscarPorId(prestamoId);
    }

    public List<CrePrestamoAprobacionResponse> listarAprobacionesPrestamo(Long prestamoId) {
        return aprobacionPrestamoService.buscarTodos().stream()
                .filter(a -> prestamoId.equals(a.getPrestamoId()))
                .collect(java.util.stream.Collectors.toList());
    }

    public SpResultado aprobarPrestamo(AprobarPrestamoRequest request) {
        return aprobarPrestamoService.ejecutar(request);
    }

    public SpResultado rechazarPrestamo(RechazarPrestamoRequest request) {
        return rechazarPrestamoService.ejecutar(request);
    }

    public SpResultado desembolsarPrestamo(DesembolsarPrestamoRequest request) {
        return desembolsarPrestamoService.ejecutar(request);
    }

    // -------------------------------------------------------------------------
    // Transferencias
    // -------------------------------------------------------------------------

    public List<TrfTransferenciaResponse> listarTransferencias() {
        return transferenciaService.buscarTodos();
    }

    public TrfTransferenciaResponse verTransferencia(Long transferenciaId) {
        return transferenciaService.buscarPorId(transferenciaId);
    }

    public SpResultado vencerTransferenciasPendientes(VencerTransferenciasPendientesRequest request) {
        return vencerPendientesService.ejecutar(request);
    }

    // -------------------------------------------------------------------------
    // Auditoría
    // -------------------------------------------------------------------------

    public List<AudBitacoraEventoResponse> listarBitacora() {
        return bitacoraService.buscarTodos();
    }

    public AudBitacoraEventoResponse verEventoBitacora(Long eventoId) {
        return bitacoraService.buscarPorId(eventoId);
    }

    public List<AudCambioDatoResponse> listarCambiosDatos() {
        return cambioDatoService.buscarTodos();
    }

    public List<AudErrorOperacionResponse> listarErroresOperacion() {
        return errorService.buscarTodos();
    }

    public void registrarEvento(RegistrarEventoRequest request) {
        registrarEventoService.ejecutar(request);
    }
}
