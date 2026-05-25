package bd2.Banco.domain.exceptions;

public class UsuarioNoEncontradoException extends EntidadNoEncontradaException {
    public UsuarioNoEncontradoException(Long id) {
        super("DOM-SEC-404", "Usuario con id=" + id + " no existe");
    }
    public UsuarioNoEncontradoException(String username) {
        super("DOM-SEC-404", "Usuario '" + username + "' no existe");
    }
}
