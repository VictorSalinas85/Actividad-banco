package bd2.Banco.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Contrato estándar de salida de todos los procedimientos almacenados del sistema.
 * Mapea los parámetros OUT: out_code, out_message, out_reference.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpResultado {

    private String code;
    private String message;
    private String reference;

    public boolean exitoso() {
        return "OK".equals(code);
    }

    public boolean fallido() {
        return !exitoso();
    }
}
