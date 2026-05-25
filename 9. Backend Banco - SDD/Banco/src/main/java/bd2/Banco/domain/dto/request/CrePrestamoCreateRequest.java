package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.TipoParticipante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrePrestamoCreateRequest {
    private Long tipoPrestamoId;
    private TipoParticipante clienteTipo;
    private Long clientePersonaId;
    private Long clienteEmpresaId;
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private Long estadoId;
    private Long cuentaDestinoDesembolsoId;
    private Long createdBy;
}
