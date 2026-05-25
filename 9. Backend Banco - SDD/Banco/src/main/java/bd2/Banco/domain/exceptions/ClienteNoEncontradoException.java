package bd2.Banco.domain.exceptions;

/** El cliente (persona natural o empresa) consultado no existe en el sistema. */
public class ClienteNoEncontradoException extends DomainException {

    public ClienteNoEncontradoException(Long clienteId) {
        super("DOM-CLI-404", "Cliente no encontrado con id: " + clienteId);
    }

    public ClienteNoEncontradoException(String identificacion) {
        super("DOM-CLI-404", "Cliente no encontrado con identificacion: " + identificacion);
    }
}
