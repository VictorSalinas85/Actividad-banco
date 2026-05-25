package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CtaCuentaUpdateRequest {
    private BigDecimal saldoActual;
    private BigDecimal limiteSobregirosAutorizado;
    private Long estadoId;
    private Long updatedBy;
}
