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
public class CrePrestamoDesembolsoCreateRequest {
    private Long prestamoId;
    private Long analistaUsuarioId;
    private Long cuentaDestinoId;
    private BigDecimal monto;
}
