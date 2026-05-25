package bd2.Banco.domain.exceptions;

/**
 * Se lanza cuando un procedimiento almacenado retorna out_code distinto de "OK".
 * El código de dominio proviene directamente del SP (ej. DOM-TRF-001, DOM-CRE-001).
 */
public class ProcedimientoBancoException extends DomainException {

    private final String referencia;

    public ProcedimientoBancoException(String codigoError, String mensaje, String referencia) {
        super(codigoError, mensaje);
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }
}
