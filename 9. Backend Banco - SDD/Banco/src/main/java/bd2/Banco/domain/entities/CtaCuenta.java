package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.TipoParticipante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cta_cuenta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CtaCuenta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "numero_cuenta", nullable = false, length = 24, unique = true) private String numeroCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_cuenta_id", nullable = false)
    private CatTipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "titular_tipo", nullable = false, length = 20) private TipoParticipante titularTipo;

    /** ID de la persona natural titular. Null si titular es empresa. */
    @Column(name = "titular_persona_id") private Long titularPersonaId;

    /** ID de la empresa titular. Null si titular es persona natural. */
    @Column(name = "titular_empresa_id") private Long titularEmpresaId;

    @Column(name = "saldo_actual", nullable = false, precision = 18, scale = 2) private BigDecimal saldoActual;
    @Column(name = "limite_sobregiro_autorizado", nullable = false, precision = 18, scale = 2) private BigDecimal limiteSobregirosAutorizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moneda_id", nullable = false)
    private CatMoneda moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private CatEstadoCuenta estado;

    @Column(name = "fecha_apertura", nullable = false) private LocalDateTime fechaApertura;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
    @Column(name = "updated_by", nullable = false) private Long updatedBy;
    @Column(name = "row_version", nullable = false) private Long rowVersion;
}
