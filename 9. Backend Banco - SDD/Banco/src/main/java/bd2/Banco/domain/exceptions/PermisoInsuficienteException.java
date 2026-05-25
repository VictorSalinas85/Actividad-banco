package bd2.Banco.domain.exceptions;

/** El usuario no tiene el rol requerido para ejecutar la operación. Código: DOM-SEC-001. */
public class PermisoInsuficienteException extends DomainException {

    public PermisoInsuficienteException(Long usuarioId) {
        super("DOM-SEC-001", "El usuario no tiene permiso para esta operacion, id: " + usuarioId);
    }
}
