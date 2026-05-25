package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecUsuarioCreateRequest {
    private String username;
    private String hashPassword;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Long estadoId;
    private Long createdBy;
}
