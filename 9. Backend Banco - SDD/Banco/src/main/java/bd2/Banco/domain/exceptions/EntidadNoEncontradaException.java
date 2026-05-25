package bd2.Banco.domain.exceptions;

public class EntidadNoEncontradaException extends DomainException {
    public EntidadNoEncontradaException(String entidad, Long id) {
        super("DOM-CRUD-404", entidad + " con id=" + id + " no existe");
    }
    public EntidadNoEncontradaException(String codigoError, String mensaje) {
        super(codigoError, mensaje);
    }
}
