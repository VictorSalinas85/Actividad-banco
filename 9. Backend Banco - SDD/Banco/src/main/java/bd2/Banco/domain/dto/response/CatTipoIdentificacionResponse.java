package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.AplicaTipoIdentificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTipoIdentificacionResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private AplicaTipoIdentificacion aplicaA;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
