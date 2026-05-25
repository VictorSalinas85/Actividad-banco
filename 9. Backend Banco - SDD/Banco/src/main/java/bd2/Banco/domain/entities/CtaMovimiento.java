package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cta_movimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CtaMovimiento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private CtaCuenta cuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_movimiento_id", nullable = false)
    private CatTipoMovimiento tipoMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_operacion_id", nullable = false)
    private CatTipoOperacion tipoOperacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canal_id", nullable = false)
    private CatCanalOperacion canal;

    @Column(name = "referencia_externa", length = 64) private String referenciaExterna;
    @Column(name = "idempotency_key", length = 64) private String idempotencyKey;
    @Column(name = "monto", nullable = false, precision = 18, scale = 2) private BigDecimal monto;
    @Column(name = "saldo_antes", nullable = false, precision = 18, scale = 2) private BigDecimal saldoAntes;
    @Column(name = "saldo_despues", nullable = false, precision = 18, scale = 2) private BigDecimal saldoDespues;
    @Column(name = "fecha_movimiento", nullable = false) private LocalDateTime fechaMovimiento;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
}
