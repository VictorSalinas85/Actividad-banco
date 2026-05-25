package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.DecisionAprobacionTransferencia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trf_transferencia_aprobacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrfTransferenciaAprobacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferencia_id", nullable = false)
    private TrfTransferencia transferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprobador_usuario_id", nullable = false)
    private SecUsuario aprobadorUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 20) private DecisionAprobacionTransferencia decision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motivo_rechazo_id")
    private CatMotivoRechazo motivoRechazo;

    @Column(name = "comentario", length = 255) private String comentario;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
}
