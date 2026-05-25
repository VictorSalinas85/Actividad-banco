package bd2.Banco.domain.exceptions;

/** La cuenta está BLOQUEADA o CANCELADA y no permite operaciones monetarias. Código: DOM-TRF-002. */
public class CuentaInactivaException extends DomainException {

    public CuentaInactivaException(String numeroCuenta) {
        super("DOM-TRF-002", "La cuenta no esta operativa: " + numeroCuenta);
    }

    public CuentaInactivaException(Long cuentaId) {
        super("DOM-TRF-002", "La cuenta no esta operativa, id: " + cuentaId);
    }
}
