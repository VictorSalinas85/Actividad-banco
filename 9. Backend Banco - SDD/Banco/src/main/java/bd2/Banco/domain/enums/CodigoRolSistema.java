package bd2.Banco.domain.enums;

/** Códigos de sec_rol usados en validaciones de los SPs (ej. ANALISTA_INTERNO en sp_cre_aprobar_prestamo). */
public enum CodigoRolSistema {
    CLIENTE_PERSONA,
    CLIENTE_EMPRESA_ADMIN,
    EMPLEADO_VENTANILLA,
    EMPLEADO_COMERCIAL,
    EMPLEADO_EMPRESA_OPERATIVO,
    SUPERVISOR_EMPRESA,
    ANALISTA_INTERNO
}
