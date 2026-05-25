package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.CategoriaProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrdProductoBancarioCreateRequest {
    private String codigoProducto;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private Boolean requiereAprobacion;
    private Boolean activo;
}
