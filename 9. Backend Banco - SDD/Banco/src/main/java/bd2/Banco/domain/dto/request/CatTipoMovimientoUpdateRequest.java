package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.NaturalezaMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatTipoMovimientoUpdateRequest {
    private String nombre;
    private NaturalezaMovimiento naturaleza;
    private Boolean activo;
}
