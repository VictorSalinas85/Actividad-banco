package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.CategoriaProducto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prd_producto_bancario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PrdProductoBancario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "codigo_producto", nullable = false, length = 40, unique = true) private String codigoProducto;
    @Column(name = "nombre_producto", nullable = false, length = 120) private String nombreProducto;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30) private CategoriaProducto categoria;

    @Column(name = "requiere_aprobacion", nullable = false) private Boolean requiereAprobacion;
    @Column(name = "activo", nullable = false) private Boolean activo;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
