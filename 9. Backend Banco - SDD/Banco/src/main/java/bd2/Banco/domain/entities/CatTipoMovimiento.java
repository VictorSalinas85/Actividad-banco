package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.NaturalezaMovimiento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cat_tipo_movimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatTipoMovimiento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "codigo", nullable = false, length = 30, unique = true) private String codigo;
    @Column(name = "nombre", nullable = false, length = 100) private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "naturaleza", nullable = false, length = 10) private NaturalezaMovimiento naturaleza;

    @Column(name = "activo", nullable = false) private Boolean activo;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
