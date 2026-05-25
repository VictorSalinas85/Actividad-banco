package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.TipoParticipante;
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
public class CrePrestamoResponse {
    private Long id;
    private Long tipoPrestamoId;
    private TipoParticipante clienteTipo;
    private Long clientePersonaId;
    private Long clienteEmpresaId;
    private BigDecimal montoSolicitado;
    private BigDecimal montoAprobado;
    private BigDecimal tasaInteres;
    private Integer plazoMeses;
    private Long estadoId;
    private LocalDateTime fechaAprobacion;
    private LocalDateTime fechaDesembolso;
    private Long cuentaDestinoDesembolsoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
