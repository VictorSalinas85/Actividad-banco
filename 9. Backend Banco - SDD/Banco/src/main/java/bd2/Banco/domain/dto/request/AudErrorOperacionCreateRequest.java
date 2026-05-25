package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudErrorOperacionCreateRequest {
    private String codigoError;
    private String modulo;
    private String mensaje;
    private String referencia;
    private Long actorUsuarioId;
    private String payload;
}
