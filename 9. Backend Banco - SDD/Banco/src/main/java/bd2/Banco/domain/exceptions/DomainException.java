package bd2.Banco.domain.exceptions;

/** Excepción base del dominio bancario. Lleva el código de dominio (ej. DOM-TRF-001) y el mensaje descriptivo. */
public class DomainException extends RuntimeException {

    private final String codigoError;

    public DomainException(String codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public DomainException(String codigoError, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
