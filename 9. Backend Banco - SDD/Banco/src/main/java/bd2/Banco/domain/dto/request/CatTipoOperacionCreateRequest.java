package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTipoOperacionCreateRequest {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
