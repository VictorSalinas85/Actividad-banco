package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.DecisionAprobacionPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrePrestamoAprobacionCreateRequest {
    private Long prestamoId;
    private Long analistaUsuarioId;
    private DecisionAprobacionPrestamo decision;
    private Long motivoRechazoId;
    private String comentario;
}
