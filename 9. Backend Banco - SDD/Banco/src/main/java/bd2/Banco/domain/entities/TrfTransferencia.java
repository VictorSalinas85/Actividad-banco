package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trf_transferencia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrfTransferencia {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_origen_id", nullable = false)
    private CtaCuenta cuentaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_destino_id", nullable = false)
    private CtaCuenta cuentaDestino;

    /** ID de la empresa que origina la transferencia corporativa. Null para personas naturales. */
    @Column(name = "empresa_id") private Long empresaId;

    @Column(name = "monto", nullable = false, precision = 18, scale = 2) private BigDecimal monto;
    @Column(name = "fecha_creacion", nullable = false) private LocalDateTime fechaCreacion;
    @Column(name = "fecha_aprobacion") private LocalDateTime fechaAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private CatEstadoTransferencia estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_usuario_id", nullable = false)
    private SecUsuario creadorUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprobador_usuario_id")
    private SecUsuario aprobadorUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canal_id", nullable = false)
    private CatCanalOperacion canal;

    @Column(name = "idempotency_key", length = 64) private String idempotencyKey;
    @Column(name = "referencia_externa", length = 64) private String referenciaExterna;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
    @Column(name = "updated_by", nullable = false) private Long updatedBy;
    @Column(name = "row_version", nullable = false) private Long rowVersion;
}
