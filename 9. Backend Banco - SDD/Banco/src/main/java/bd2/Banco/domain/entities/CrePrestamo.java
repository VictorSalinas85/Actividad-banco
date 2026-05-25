package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.TipoParticipante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cre_prestamo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CrePrestamo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_prestamo_id", nullable = false)
    private CatTipoPrestamo tipoPrestamo;

    @Enumerated(EnumType.STRING)
    @Column(name = "cliente_tipo", nullable = false, length = 20) private TipoParticipante clienteTipo;

    /** ID de la persona natural. Null si el cliente es empresa. */
    @Column(name = "cliente_persona_id") private Long clientePersonaId;

    /** ID de la empresa. Null si el cliente es persona natural. */
    @Column(name = "cliente_empresa_id") private Long clienteEmpresaId;

    @Column(name = "monto_solicitado", nullable = false, precision = 18, scale = 2) private BigDecimal montoSolicitado;
    @Column(name = "monto_aprobado", precision = 18, scale = 2) private BigDecimal montoAprobado;
    @Column(name = "tasa_interes", precision = 7, scale = 4) private BigDecimal tasaInteres;
    @Column(name = "plazo_meses", nullable = false) private Integer plazoMeses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private CatEstadoPrestamo estado;

    @Column(name = "fecha_aprobacion") private LocalDateTime fechaAprobacion;
    @Column(name = "fecha_desembolso") private LocalDateTime fechaDesembolso;

    /** FK a cta_cuenta donde se desembolsa. Null hasta la solicitud de desembolso. */
    @Column(name = "cuenta_destino_desembolso_id") private Long cuentaDestinoDesembolsoId;

    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
    @Column(name = "updated_by", nullable = false) private Long updatedBy;
    @Column(name = "row_version", nullable = false) private Long rowVersion;
}
