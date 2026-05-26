package bd2.Banco.application.usecase;

import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.*;
import bd2.Banco.domain.services.procedures.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Rol: EMPLEADO_COMERCIAL
 * Operaciones: crear clientes empresa, abrir cuentas para empresas,
 * solicitar préstamos en nombre de clientes y asociar usuarios a empresas.
 */
@Service
@RequiredArgsConstructor
public class EmpleadoComercialUseCase {

    private final CliPersonaNaturalCrudService personaService;
    private final CliEmpresaCrudService empresaService;
    private final CtaCuentaCrudService cuentaService;
    private final CrePrestamoCrudService prestamoService;
    private final SpCliCrearEmpresaService crearEmpresaService;
    private final SpCtaAbrirCuentaService abrirCuentaService;
    private final SpCreSolicitarPrestamoService solicitarPrestamoService;
    private final SpCliAsociarUsuarioEmpresaService asociarUsuarioService;

    // -------------------------------------------------------------------------
    // Clientes
    // -------------------------------------------------------------------------

    public SpResultado crearEmpresa(CrearEmpresaRequest request) {
        return crearEmpresaService.ejecutar(request);
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

    public List<CliEmpresaResponse> listarEmpresas() {
        return empresaService.buscarTodos();
    }

    public SpResultado asociarUsuarioAEmpresa(AsociarUsuarioEmpresaRequest request) {
        return asociarUsuarioService.ejecutar(request);
    }

    // -------------------------------------------------------------------------
    // Cuentas
    // -------------------------------------------------------------------------

    public SpResultado abrirCuenta(AbrirCuentaRequest request) {
        return abrirCuentaService.ejecutar(request);
    }

    public CtaCuentaResponse verCuenta(Long cuentaId) {
        return cuentaService.buscarPorId(cuentaId);
    }

    public List<CtaCuentaResponse> listarCuentas() {
        return cuentaService.buscarTodos();
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

    public List<CrePrestamoResponse> listarPrestamos() {
        return prestamoService.buscarTodos();
    }
}
