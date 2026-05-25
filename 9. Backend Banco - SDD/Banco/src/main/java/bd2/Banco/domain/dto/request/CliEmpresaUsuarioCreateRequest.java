package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CliEmpresaUsuarioCreateRequest {
    private Long empresaId;
    private Long usuarioId;
    private Boolean activo;
    private Long createdBy;
}
