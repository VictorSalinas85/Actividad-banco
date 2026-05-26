package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.ConsultarPendientesEmpresaRequest;
import bd2.Banco.domain.dto.request.CrearTransferenciaRequest;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.CtaCuentaCrudService;
import bd2.Banco.domain.services.crud.CtaMovimientoCrudService;
import bd2.Banco.domain.services.crud.TrfTransferenciaCrudService;
import bd2.Banco.domain.services.procedures.SpTrfConsultarPendientesEmpresaService;
import bd2.Banco.domain.services.procedures.SpTrfCrearTransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rol: EMPLEADO_EMPRESA_OPERATIVO
 * Operaciones: crear transferencias desde cuentas de la empresa y consultar
 * transferencias pendientes de aprobación.
 */
@Service
@RequiredArgsConstructor
public class EmpleadoEmpresaOperativoUseCase {

    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;
    private final TrfTransferenciaCrudService transferenciaService;
    private final SpTrfCrearTransferenciaService crearTransferenciaService;
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

    public SpResultado crearTransferencia(CrearTransferenciaRequest request) {
        return crearTransferenciaService.ejecutar(request);
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
}
