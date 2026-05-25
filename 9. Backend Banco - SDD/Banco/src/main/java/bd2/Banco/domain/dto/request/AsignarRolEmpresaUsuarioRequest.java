package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AsignarRolEmpresaUsuarioRequest {
    private Long empresaUsuarioId;
    private String rolEmpresaCodigo;
    private Long actorUsuarioId;
}
