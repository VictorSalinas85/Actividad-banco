package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.NaturalezaMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTipoMovimientoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private NaturalezaMovimiento naturaleza;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
