package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class RegistrarEventoRequest {
    private String tipoOperacionCodigo;
    private Long idUsuario;
    private Long rolUsuarioId;
    private String productoTipo;
    private String productoId;
    private String datosDetalle;
}
