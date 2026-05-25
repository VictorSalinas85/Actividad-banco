package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CliPersonaNaturalUpdateRequest {
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
    private Long estadoId;
    private Long updatedBy;
}
