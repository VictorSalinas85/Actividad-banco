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
public class TrfTransferenciaCreateRequest {
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private Long empresaId;
    private BigDecimal monto;
    private Long estadoId;
    private Long creadorUsuarioId;
    private Long canalId;
    private String idempotencyKey;
    private String referenciaExterna;
    private LocalDateTime fechaCreacion;
    private Long createdBy;
}
