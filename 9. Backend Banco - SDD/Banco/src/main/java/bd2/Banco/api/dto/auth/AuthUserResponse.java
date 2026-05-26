package bd2.Banco.api.dto.auth;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AuthUserResponse {
    private final Long id;
    private final String username;
    private final String nombreCompleto;
    private final String email;
    private final String telefono;
    private final Long estadoId;
    private final List<String> roles;
    private final LocalDateTime ultimoLoginAt;
    private final LocalDateTime createdAt;
}
