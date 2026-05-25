package bd2.Banco.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatParametroNegocioResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String valorTexto;
    private BigDecimal valorNumerico;
    private Boolean valorBooleano;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
