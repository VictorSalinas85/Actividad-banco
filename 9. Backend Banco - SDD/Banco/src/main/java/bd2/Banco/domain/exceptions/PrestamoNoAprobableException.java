package bd2.Banco.domain.exceptions;

/** El préstamo no puede ser aprobado/rechazado/desembolsado en su estado actual. Código: DOM-CRE-001. */
public class PrestamoNoAprobableException extends DomainException {

    public PrestamoNoAprobableException(Long prestamoId) {
        super("DOM-CRE-001", "El prestamo no puede procesarse en su estado actual, id: " + prestamoId);
    }
}
