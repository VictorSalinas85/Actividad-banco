package bd2.Banco.domain.exceptions;

/** La identificación (DNI, cédula o NIT) ya existe en el sistema. Código de dominio: DOM-CLI-001. */
public class IdentificacionDuplicadaException extends DomainException {

    public IdentificacionDuplicadaException(String identificacion) {
        super("DOM-CLI-001", "La identificacion ya existe en el sistema: " + identificacion);
    }
}
