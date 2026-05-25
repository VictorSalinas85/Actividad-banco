package bd2.Banco.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudErrorOperacionResponse {
    private Long id;
    private String codigoError;
    private String modulo;
    private String mensaje;
    private String referencia;
    private Long actorUsuarioId;
    private String payload;
    private LocalDateTime createdAt;
}
