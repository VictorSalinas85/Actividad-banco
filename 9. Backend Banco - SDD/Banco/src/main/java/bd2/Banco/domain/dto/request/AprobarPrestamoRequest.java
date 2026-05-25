package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AprobarPrestamoRequest {
    private Long prestamoId;
    private BigDecimal montoAprobado;
    private BigDecimal tasaInteres;
    private Long analistaUsuarioId;
}
