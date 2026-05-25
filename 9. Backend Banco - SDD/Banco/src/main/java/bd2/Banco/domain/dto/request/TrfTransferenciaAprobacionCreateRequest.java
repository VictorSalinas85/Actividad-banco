package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.DecisionAprobacionTransferencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrfTransferenciaAprobacionCreateRequest {
    private Long transferenciaId;
    private Long aprobadorUsuarioId;
    private DecisionAprobacionTransferencia decision;
    private Long motivoRechazoId;
    private String comentario;
}
