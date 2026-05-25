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
public class SecUsuarioResponse {
    private Long id;
    private String username;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Long estadoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
