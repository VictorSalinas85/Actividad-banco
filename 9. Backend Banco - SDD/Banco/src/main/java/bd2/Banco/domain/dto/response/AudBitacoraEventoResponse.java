package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.ProductoTipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudBitacoraEventoResponse {
    private Long id;
    private Long tipoOperacionId;
    private LocalDateTime fechaHoraOperacion;
    private Long idUsuario;
    private Long rolUsuarioId;
    private ProductoTipo productoTipo;
    private String productoId;
    private String datosDetalle;
    private String hashIntegridad;
    private LocalDateTime createdAt;
}
