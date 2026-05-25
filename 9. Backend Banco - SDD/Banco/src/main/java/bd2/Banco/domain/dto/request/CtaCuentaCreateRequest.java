package bd2.Banco.domain.dto.request;

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
public class CtaCuentaCreateRequest {
    private String numeroCuenta;
    private Long tipoCuentaId;
    private TipoParticipante titularTipo;
    private Long titularPersonaId;
    private Long titularEmpresaId;
    private BigDecimal saldoActual;
    private BigDecimal limiteSobregirosAutorizado;
    private Long monedaId;
    private Long estadoId;
    private LocalDateTime fechaApertura;
    private Long createdBy;
}
