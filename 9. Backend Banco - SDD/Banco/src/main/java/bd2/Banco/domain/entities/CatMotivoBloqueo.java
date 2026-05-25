package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cat_motivo_bloqueo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatMotivoBloqueo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "codigo", nullable = false, length = 40, unique = true) private String codigo;
    @Column(name = "nombre", nullable = false, length = 100) private String nombre;
    @Column(name = "descripcion", length = 255) private String descripcion;
    @Column(name = "activo", nullable = false) private Boolean activo;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
