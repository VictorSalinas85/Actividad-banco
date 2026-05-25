package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class RechazarTransferenciaRequest {
    private Long transferenciaId;
    private Long aprobadorUsuarioId;
    private String motivoRechazoCodigo;
    private String comentario;
}
