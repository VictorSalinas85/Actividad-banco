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
public class CliEmpresaResponse {
    private Long id;
    private Long tipoIdentificacionId;
    private String nit;
    private String razonSocial;
    private String email;
    private String telefono;
    private String direccion;
    private Long representantePersonaId;
    private Long estadoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
