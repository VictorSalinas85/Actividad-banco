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
public class TrfTransferenciaResponse {
    private Long id;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private Long empresaId;
    private BigDecimal monto;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAprobacion;
    private Long estadoId;
    private Long creadorUsuarioId;
    private Long aprobadorUsuarioId;
    private Long canalId;
    private String idempotencyKey;
    private String referenciaExterna;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
