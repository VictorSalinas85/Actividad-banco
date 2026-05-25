package bd2.Banco.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CtaMovimientoResponse {
    private Long id;
    private Long cuentaId;
    private Long tipoMovimientoId;
    private Long tipoOperacionId;
    private Long canalId;
    private String referenciaExterna;
    private String idempotencyKey;
    private BigDecimal monto;
    private BigDecimal saldoAntes;
    private BigDecimal saldoDespues;
    private LocalDateTime fechaMovimiento;
    private LocalDateTime createdAt;
}
