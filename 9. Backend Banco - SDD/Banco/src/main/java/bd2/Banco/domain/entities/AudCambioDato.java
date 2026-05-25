package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.AccionAuditoria;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "aud_cambio_dato")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AudCambioDato {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "tabla", nullable = false, length = 120) private String tabla;
    @Column(name = "registro_id", nullable = false, length = 64) private String registroId;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false, length = 20) private AccionAuditoria accion;

    @Column(name = "old_data", columnDefinition = "json") private String oldData;
    @Column(name = "new_data", columnDefinition = "json") private String newData;
    @Column(name = "sql_user", nullable = false, length = 120) private String sqlUser;
    @Column(name = "host_name", length = 255) private String hostName;
    @Column(name = "trx_ref", length = 120) private String trxRef;
    @CreationTimestamp @Column(name = "changed_at", nullable = false, updatable = false) private LocalDateTime changedAt;
}
