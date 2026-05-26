package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.AprobarTransferenciaRequest;
import bd2.Banco.domain.dto.request.ConsultarPendientesEmpresaRequest;
import bd2.Banco.domain.dto.request.RechazarTransferenciaRequest;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.CtaCuentaCrudService;
import bd2.Banco.domain.services.crud.CtaMovimientoCrudService;
import bd2.Banco.domain.services.crud.TrfTransferenciaAprobacionCrudService;
import bd2.Banco.domain.services.crud.TrfTransferenciaCrudService;
import bd2.Banco.domain.services.procedures.SpTrfAprobarTransferenciaService;
import bd2.Banco.domain.services.procedures.SpTrfConsultarPendientesEmpresaService;
import bd2.Banco.domain.services.procedures.SpTrfRechazarTransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rol: SUPERVISOR_EMPRESA
 * Operaciones: aprobar y rechazar transferencias empresariales que superan
 * el umbral de aprobación automática, y consultar el estado de las mismas.
 */
@Service
@RequiredArgsConstructor
public class SupervisorEmpresaUseCase {

    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;
    private final TrfTransferenciaCrudService transferenciaService;
    private final TrfTransferenciaAprobacionCrudService aprobacionService;
    private final SpTrfAprobarTransferenciaService aprobarTransferenciaService;
    private final SpTrfRechazarTransferenciaService rechazarTransferenciaService;
    private final SpTrfConsultarPendientesEmpresaService consultarPendientesService;

    // -------------------------------------------------------------------------
    // Cuentas
    // -------------------------------------------------------------------------

    public CtaCuentaResponse verCuenta(Long cuentaId) {
        return cuentaService.buscarPorId(cuentaId);
    }

    public List<CtaMovimientoResponse> listarMovimientosCuenta(Long cuentaId) {
        return movimientoService.buscarTodos().stream()
                .filter(m -> cuentaId.equals(m.getCuentaId()))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Transferencias
    // -------------------------------------------------------------------------

    public SpResultado aprobarTransferencia(AprobarTransferenciaRequest request) {
        return aprobarTransferenciaService.ejecutar(request);
    }

    public SpResultado rechazarTransferencia(RechazarTransferenciaRequest request) {
        return rechazarTransferenciaService.ejecutar(request);
    }

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

    public List<TrfTransferenciaAprobacionResponse> listarAprobacionesTransferencia(Long transferenciaId) {
        return aprobacionService.buscarTodos().stream()
                .filter(a -> transferenciaId.equals(a.getTransferenciaId()))
                .collect(Collectors.toList());
    }
}
