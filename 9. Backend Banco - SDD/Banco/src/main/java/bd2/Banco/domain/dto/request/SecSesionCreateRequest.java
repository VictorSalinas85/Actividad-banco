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
public class SecSesionCreateRequest {
    private String token;
    private Long usuarioId;
    private Long estadoSesionId;
    private LocalDateTime expiraAt;
}
