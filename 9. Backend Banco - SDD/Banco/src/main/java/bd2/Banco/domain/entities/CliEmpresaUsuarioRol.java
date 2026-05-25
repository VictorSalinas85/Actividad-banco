package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cli_empresa_usuario_rol")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CliEmpresaUsuarioRol {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_usuario_id", nullable = false)
    private CliEmpresaUsuario empresaUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_empresa_id", nullable = false)
    private CatRolEmpresa rolEmpresa;

    @Column(name = "activo", nullable = false) private Boolean activo;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(name = "created_by", nullable = false) private Long createdBy;
}
