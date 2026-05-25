package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.DecisionAprobacionPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrePrestamoAprobacionResponse {
    private Long id;
    private Long prestamoId;
    private Long analistaUsuarioId;
    private DecisionAprobacionPrestamo decision;
    private Long motivoRechazoId;
    private String comentario;
    private LocalDateTime createdAt;
}
