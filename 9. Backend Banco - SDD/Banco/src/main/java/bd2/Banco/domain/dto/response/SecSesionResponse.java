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
public class SecSesionResponse {
    private Long id;
    private String token;
    private Long usuarioId;
    private Long estadoSesionId;
    private LocalDateTime expiraAt;
    private LocalDateTime revocadaAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
