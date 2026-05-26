package bd2.Banco.domain.exceptions;

/** La transferencia está en un estado que no permite la operación solicitada. Código: DOM-TRF-003. */
public class TransferenciaInvalidaException extends DomainException {

    public TransferenciaInvalidaException(Long transferenciaId) {
        super("DOM-TRF-003", "La transferencia no puede procesarse en su estado actual, id: " + transferenciaId);
    }

    public TransferenciaInvalidaException(String razon) {
        super("DOM-TRF-003", razon);
    }
}
