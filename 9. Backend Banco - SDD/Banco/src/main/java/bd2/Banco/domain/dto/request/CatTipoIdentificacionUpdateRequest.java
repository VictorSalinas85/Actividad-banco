package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.AplicaTipoIdentificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTipoIdentificacionUpdateRequest {
    private String nombre;
    private AplicaTipoIdentificacion aplicaA;
    private Boolean activo;
}
