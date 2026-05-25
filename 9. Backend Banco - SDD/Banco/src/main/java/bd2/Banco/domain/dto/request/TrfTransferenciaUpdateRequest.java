package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrfTransferenciaUpdateRequest {
    private Long estadoId;
    private Long aprobadorUsuarioId;
    private LocalDateTime fechaAprobacion;
    private Long updatedBy;
}
