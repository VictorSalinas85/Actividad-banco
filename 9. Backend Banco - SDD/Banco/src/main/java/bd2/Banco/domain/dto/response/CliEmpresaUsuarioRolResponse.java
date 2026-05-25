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
public class CliEmpresaUsuarioRolResponse {
    private Long id;
    private Long empresaUsuarioId;
    private Long rolEmpresaId;
    private Boolean activo;
    private LocalDateTime createdAt;
}
