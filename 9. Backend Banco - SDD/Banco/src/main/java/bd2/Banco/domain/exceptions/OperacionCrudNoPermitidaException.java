package bd2.Banco.domain.exceptions;

public class OperacionCrudNoPermitidaException extends DomainException {
    public OperacionCrudNoPermitidaException(String entidad, String operacion) {
        super("DOM-CRUD-403", "Operacion " + operacion + " no permitida sobre " + entidad);
    }
}
