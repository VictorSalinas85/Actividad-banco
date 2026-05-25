package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatEstadoUsuarioCreateRequest {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Integer ordenVisual;
    private LocalDateTime vigentDesde;
    private LocalDateTime vigentHasta;
}
