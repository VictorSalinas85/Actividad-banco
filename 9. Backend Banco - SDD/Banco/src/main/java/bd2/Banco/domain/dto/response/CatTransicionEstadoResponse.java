package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.EntidadTransicion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTransicionEstadoResponse {
    private Long id;
    private EntidadTransicion entidad;
    private String estadoOrigenCodigo;
    private String estadoDestinoCodigo;
    private String rolRequeridoCodigo;
    private Boolean requiereMotivo;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
