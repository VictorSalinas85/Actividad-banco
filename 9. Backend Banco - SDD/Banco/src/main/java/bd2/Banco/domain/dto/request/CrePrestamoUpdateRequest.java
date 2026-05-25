package bd2.Banco.domain.dto.request;

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
public class CrePrestamoUpdateRequest {
    private BigDecimal montoAprobado;
    private BigDecimal tasaInteres;
    private Long estadoId;
    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaDesembolso;
    private Long cuentaDestinoDesembolsoId;
    private Long updatedBy;
}
