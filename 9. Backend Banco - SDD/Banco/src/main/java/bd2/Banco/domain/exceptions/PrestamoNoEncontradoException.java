package bd2.Banco.domain.exceptions;

public class PrestamoNoEncontradoException extends EntidadNoEncontradaException {
    public PrestamoNoEncontradoException(Long id) {
        super("DOM-CRE-404", "Prestamo con id=" + id + " no existe");
    }
}
