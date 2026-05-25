package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.EntidadTransicion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTransicionEstadoCreateRequest {
    private EntidadTransicion entidad;
    private String estadoOrigenCodigo;
    private String estadoDestinoCodigo;
    private String rolRequeridoCodigo;
    private Boolean requiereMotivo;
    private Boolean activo;
}
