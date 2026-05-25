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
public class CtaCuentaResponse {
    private Long id;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
