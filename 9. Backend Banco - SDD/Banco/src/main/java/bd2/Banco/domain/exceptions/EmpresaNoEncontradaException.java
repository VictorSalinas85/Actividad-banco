package bd2.Banco.domain.exceptions;

public class EmpresaNoEncontradaException extends EntidadNoEncontradaException {
    public EmpresaNoEncontradaException(Long id) {
        super("DOM-CLI-404E", "Empresa con id=" + id + " no existe");
    }
}
