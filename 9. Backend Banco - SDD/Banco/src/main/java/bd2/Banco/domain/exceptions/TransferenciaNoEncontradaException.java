package bd2.Banco.domain.exceptions;

public class TransferenciaNoEncontradaException extends EntidadNoEncontradaException {
    public TransferenciaNoEncontradaException(Long id) {
        super("DOM-TRF-404", "Transferencia con id=" + id + " no existe");
    }
}
