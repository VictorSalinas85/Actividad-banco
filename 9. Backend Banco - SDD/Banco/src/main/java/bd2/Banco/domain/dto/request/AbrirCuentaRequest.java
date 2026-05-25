package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AbrirCuentaRequest {
    private String titularTipo;
    private Long titularId;
    private String tipoCuentaCodigo;
    private String monedaCodigo;
    private BigDecimal limiteSobregiro;
    private Long actorUsuarioId;
}
