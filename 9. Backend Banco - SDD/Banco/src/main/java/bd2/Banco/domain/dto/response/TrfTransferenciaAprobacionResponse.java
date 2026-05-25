package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.DecisionAprobacionTransferencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrfTransferenciaAprobacionResponse {
    private Long id;
    private Long transferenciaId;
    private Long aprobadorUsuarioId;
    private DecisionAprobacionTransferencia decision;
    private Long motivoRechazoId;
    private String comentario;
    private LocalDateTime createdAt;
}
