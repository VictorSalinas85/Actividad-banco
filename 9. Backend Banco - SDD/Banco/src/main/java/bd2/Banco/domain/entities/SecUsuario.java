package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sec_usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SecUsuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "username", nullable = false, length = 80, unique = true) private String username;
    @Column(name = "hash_password", nullable = false, length = 255) private String hashPassword;
    @Column(name = "nombre_completo", nullable = false, length = 180) private String nombreCompleto;
    @Column(name = "email", nullable = false, length = 180, unique = true) private String email;
    @Column(name = "telefono", length = 20) private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private CatEstadoUsuario estado;

    @Column(name = "ultimo_login_at") private LocalDateTime ultimoLoginAt;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
    @Column(name = "updated_by", nullable = false) private Long updatedBy;
    @Column(name = "row_version", nullable = false) private Long rowVersion;
}
