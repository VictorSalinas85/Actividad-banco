package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "aud_error_operacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AudErrorOperacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "codigo_error", nullable = false, length = 32) private String codigoError;
    @Column(name = "modulo", nullable = false, length = 20) private String modulo;
    @Column(name = "mensaje", nullable = false, length = 255) private String mensaje;
    @Column(name = "referencia", length = 64) private String referencia;
    @Column(name = "actor_usuario_id") private Long actorUsuarioId;

    @Column(name = "payload", columnDefinition = "json") private String payload;

    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
}
