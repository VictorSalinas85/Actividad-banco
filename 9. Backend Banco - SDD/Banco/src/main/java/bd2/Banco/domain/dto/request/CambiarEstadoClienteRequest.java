package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class CambiarEstadoClienteRequest {
    private String clienteTipo;
    private Long clienteId;
    private String estadoCodigo;
    private Long actorUsuarioId;
}
