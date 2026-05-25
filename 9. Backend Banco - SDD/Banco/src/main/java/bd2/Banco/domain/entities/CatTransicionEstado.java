package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.EntidadTransicion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cat_transicion_estado")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatTransicionEstado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidad", nullable = false, length = 30) private EntidadTransicion entidad;

    @Column(name = "estado_origen_codigo", nullable = false, length = 40) private String estadoOrigenCodigo;
    @Column(name = "estado_destino_codigo", nullable = false, length = 40) private String estadoDestinoCodigo;
    @Column(name = "rol_requerido_codigo", length = 50) private String rolRequeridoCodigo;
    @Column(name = "requiere_motivo", nullable = false) private Boolean requiereMotivo;
    @Column(name = "activo", nullable = false) private Boolean activo;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
