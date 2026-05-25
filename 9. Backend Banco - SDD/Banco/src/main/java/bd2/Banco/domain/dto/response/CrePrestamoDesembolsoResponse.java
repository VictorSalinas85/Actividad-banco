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
public class CrePrestamoDesembolsoResponse {
    private Long id;
    private Long prestamoId;
    private Long analistaUsuarioId;
    private Long cuentaDestinoId;
    private BigDecimal monto;
    private LocalDateTime createdAt;
}
