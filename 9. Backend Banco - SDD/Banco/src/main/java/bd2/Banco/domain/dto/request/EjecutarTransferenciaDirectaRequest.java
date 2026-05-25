package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class EjecutarTransferenciaDirectaRequest {
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private BigDecimal monto;
    private String canalCodigo;
    private String idempotencyKey;
    private Long usuarioId;
}
