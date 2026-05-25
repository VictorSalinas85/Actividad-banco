package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class SolicitarPrestamoRequest {
    private String clienteTipo;
    private Long clienteId;
    private String tipoPrestamoCodigo;
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private Long cuentaDestinoId;
    private Long actorUsuarioId;
}
