package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.CategoriaProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrdProductoBancarioResponse {
    private Long id;
    private String codigoProducto;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private Boolean requiereAprobacion;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
