package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTransicionEstadoUpdateRequest {
    private String rolRequeridoCodigo;
    private Boolean requiereMotivo;
    private Boolean activo;
}
