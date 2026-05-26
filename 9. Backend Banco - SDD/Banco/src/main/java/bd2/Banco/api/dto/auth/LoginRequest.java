package bd2.Banco.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El usuario o email es requerido")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
