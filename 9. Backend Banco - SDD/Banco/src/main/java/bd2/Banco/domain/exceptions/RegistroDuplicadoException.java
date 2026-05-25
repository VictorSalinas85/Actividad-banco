package bd2.Banco.domain.exceptions;

public class RegistroDuplicadoException extends DomainException {
    public RegistroDuplicadoException(String campo, String valor) {
        super("DOM-CRUD-409", "Ya existe un registro con " + campo + "='" + valor + "'");
    }
}
