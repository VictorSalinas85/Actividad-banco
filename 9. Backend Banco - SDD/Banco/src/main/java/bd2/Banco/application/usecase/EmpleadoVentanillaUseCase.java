package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.CliEmpresaCrudService;
import bd2.Banco.domain.services.crud.CliPersonaNaturalCrudService;
import bd2.Banco.domain.services.crud.CtaCuentaCrudService;
import bd2.Banco.domain.services.crud.CtaMovimientoCrudService;
import bd2.Banco.domain.services.procedures.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rol: EMPLEADO_VENTANILLA
 * Operaciones: crear clientes persona, abrir cuentas, consignar, retirar
 * y ejecutar transferencias directas de bajo monto.
 */
@Service
@RequiredArgsConstructor
public class EmpleadoVentanillaUseCase {

    private final CliPersonaNaturalCrudService personaService;
    private final CliEmpresaCrudService empresaService;
    private final CtaCuentaCrudService cuentaService;
    private final CtaMovimientoCrudService movimientoService;
    private final SpCliCrearPersonaService crearPersonaService;
    private final SpCtaAbrirCuentaService abrirCuentaService;
    private final SpCtaConsignarService consignarService;
    private final SpCtaRetirarService retirarService;
    private final SpTrfEjecutarTransferenciaDirectaService transferenciaDirectaService;

    // -------------------------------------------------------------------------
    // Clientes
    // -------------------------------------------------------------------------

    public SpResultado crearPersona(CrearPersonaRequest request) {
        return crearPersonaService.ejecutar(request);
    }

    public CliPersonaNaturalResponse buscarPersona(Long personaId) {
        return personaService.buscarPorId(personaId);
    }

    public List<CliPersonaNaturalResponse> listarPersonas() {
        return personaService.buscarTodos();
    }

    public CliEmpresaResponse buscarEmpresa(Long empresaId) {
        return empresaService.buscarPorId(empresaId);
    }

    // -------------------------------------------------------------------------
    // Cuentas
    // -------------------------------------------------------------------------

    public SpResultado abrirCuenta(AbrirCuentaRequest request) {
        return abrirCuentaService.ejecutar(request);
    }

    public SpResultado consignar(ConsignarRequest request) {
        return consignarService.ejecutar(request);
    }

    public SpResultado retirar(RetirarRequest request) {
        return retirarService.ejecutar(request);
    }

    public CtaCuentaResponse verCuenta(Long cuentaId) {
        return cuentaService.buscarPorId(cuentaId);
    }

    public List<CtaCuentaResponse> listarCuentas() {
        return cuentaService.buscarTodos();
    }

    public List<CtaMovimientoResponse> listarMovimientosCuenta(Long cuentaId) {
        return movimientoService.buscarTodos().stream()
                .filter(m -> cuentaId.equals(m.getCuentaId()))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Transferencias
    // -------------------------------------------------------------------------

    public SpResultado ejecutarTransferenciaDirecta(EjecutarTransferenciaDirectaRequest request) {
        return transferenciaDirectaService.ejecutar(request);
    }
}
