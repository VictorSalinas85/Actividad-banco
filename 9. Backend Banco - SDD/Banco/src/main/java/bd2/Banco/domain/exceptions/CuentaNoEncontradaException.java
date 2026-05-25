package bd2.Banco.domain.exceptions;

public class CuentaNoEncontradaException extends EntidadNoEncontradaException {
    public CuentaNoEncontradaException(Long id) {
        super("DOM-CTA-404", "Cuenta con id=" + id + " no existe");
    }
    public CuentaNoEncontradaException(String numeroCuenta) {
        super("DOM-CTA-404", "Cuenta con numero=" + numeroCuenta + " no existe");
    }
}
