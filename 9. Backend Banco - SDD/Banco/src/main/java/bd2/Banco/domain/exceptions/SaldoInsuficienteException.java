package bd2.Banco.domain.exceptions;

/** Saldo disponible insuficiente para la operación solicitada. Código: DOM-TRF-001. */
public class SaldoInsuficienteException extends DomainException {

    public SaldoInsuficienteException(Long cuentaId) {
        super("DOM-TRF-001", "Saldo insuficiente en la cuenta id: " + cuentaId);
    }
}
