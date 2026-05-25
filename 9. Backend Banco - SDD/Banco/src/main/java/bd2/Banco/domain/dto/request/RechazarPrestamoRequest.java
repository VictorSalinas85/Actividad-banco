package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class RechazarPrestamoRequest {
    private Long prestamoId;
    private String motivoRechazoCodigo;
    private String comentario;
    private Long analistaUsuarioId;
}
