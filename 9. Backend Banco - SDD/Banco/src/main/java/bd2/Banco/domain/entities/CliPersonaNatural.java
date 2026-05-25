package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cli_persona_natural")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CliPersonaNatural {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_identificacion_id", nullable = false)
    private CatTipoIdentificacion tipoIdentificacion;

    @Column(name = "identificacion", nullable = false, length = 30) private String identificacion;
    @Column(name = "nombre_completo", nullable = false, length = 180) private String nombreCompleto;
    @Column(name = "email", nullable = false, length = 180) private String email;
    @Column(name = "telefono", nullable = false, length = 20) private String telefono;
    @Column(name = "fecha_nacimiento", nullable = false) private LocalDate fechaNacimiento;
    @Column(name = "direccion", nullable = false, length = 255) private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private CatEstadoUsuario estado;

    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
    @Column(name = "updated_by", nullable = false) private Long updatedBy;
    @Column(name = "row_version", nullable = false) private Long rowVersion;
}
