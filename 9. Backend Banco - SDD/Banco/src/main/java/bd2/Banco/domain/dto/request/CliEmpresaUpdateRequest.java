package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CliEmpresaUpdateRequest {
    private String razonSocial;
    private String email;
    private String telefono;
    private String direccion;
    private Long representantePersonaId;
    private Long estadoId;
    private Long updatedBy;
}
