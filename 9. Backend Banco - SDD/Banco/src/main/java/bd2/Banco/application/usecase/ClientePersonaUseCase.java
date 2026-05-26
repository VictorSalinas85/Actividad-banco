package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.CrearTransferenciaRequest;
import bd2.Banco.domain.dto.request.SolicitarPrestamoRequest;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import bd2.Banco.domain.services.procedures.SpCreSolicitarPrestamoService;
import bd2.Banco.domain.services.procedures.SpTrfCrearTransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rol: CLIENTE_PERSONA
 * Operaciones: consultar perfil y cuentas propias, solicitar préstamos,
 * crear y consultar transferencias propias.
 */
@Service
@RequiredArgsConstructor
public class ClientePersonaUseCase {

    private final CliPersonaNaturalCrudService personaService;
    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;
    private final CrePrestamoCrudService prestamoService;
    private final TrfTransferenciaCrudService transferenciaService;
    private final SpCreSolicitarPrestamoService solicitarPrestamoService;
    private final SpTrfCrearTransferenciaService crearTransferenciaService;

    // -------------------------------------------------------------------------
    // Perfil
    // -------------------------------------------------------------------------

    public CliPersonaNaturalResponse verPerfil(Long personaId) {
        return personaService.buscarPorId(personaId);
    }

    // -------------------------------------------------------------------------
    // Cuentas
    // -------------------------------------------------------------------------

    public List<CtaCuentaResponse> listarMisCuentas(Long personaId) {
        return cuentaService.buscarTodos().stream()
                .filter(c -> personaId.equals(c.getTitularPersonaId()))
                .collect(Collectors.toList());
    }

    public CtaCuentaResponse verCuenta(Long cuentaId) {
        return cuentaService.buscarPorId(cuentaId);
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

    public List<CrePrestamoResponse> listarMisPrestamos(Long personaId) {
        return prestamoService.buscarTodos().stream()
                .filter(p -> personaId.equals(p.getClientePersonaId()))
                .collect(Collectors.toList());
    }

    public CrePrestamoResponse verPrestamo(Long prestamoId) {
        return prestamoService.buscarPorId(prestamoId);
    }

    // -------------------------------------------------------------------------
    // Transferencias
    // -------------------------------------------------------------------------

    public SpResultado crearTransferencia(CrearTransferenciaRequest request) {
        return crearTransferenciaService.ejecutar(request);
    }

    public List<TrfTransferenciaResponse> listarMisTransferencias(Long usuarioId) {
        return transferenciaService.buscarTodos().stream()
                .filter(t -> usuarioId.equals(t.getCreadorUsuarioId()))
                .collect(Collectors.toList());
    }

    public TrfTransferenciaResponse verTransferencia(Long transferenciaId) {
        return transferenciaService.buscarPorId(transferenciaId);
    }
}
