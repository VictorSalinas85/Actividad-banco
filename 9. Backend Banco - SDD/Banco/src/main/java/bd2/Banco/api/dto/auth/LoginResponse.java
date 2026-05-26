package bd2.Banco.api.dto.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginResponse {
    private final String token;
    private final String type;
    private final Long userId;
    private final String username;
    private final String nombreCompleto;
    private final String email;
    private final List<String> roles;
    private final long expiresIn;
}
