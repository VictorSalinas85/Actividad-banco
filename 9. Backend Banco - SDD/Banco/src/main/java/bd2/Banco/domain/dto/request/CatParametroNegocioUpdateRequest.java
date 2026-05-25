package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatParametroNegocioUpdateRequest {
    private String nombre;
    private String descripcion;
    private String valorTexto;
    private BigDecimal valorNumerico;
    private Boolean valorBooleano;
    private Boolean activo;
}
