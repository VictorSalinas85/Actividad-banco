package bd2.Banco.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cat_parametro_negocio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatParametroNegocio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @Column(name = "codigo", nullable = false, length = 80, unique = true) private String codigo;
    @Column(name = "nombre", nullable = false, length = 120) private String nombre;
    @Column(name = "descripcion", length = 255) private String descripcion;
    @Column(name = "valor_texto", length = 255) private String valorTexto;
    @Column(name = "valor_numerico", precision = 18, scale = 2) private BigDecimal valorNumerico;
    @Column(name = "valor_booleano") private Boolean valorBooleano;
    @Column(name = "activo", nullable = false) private Boolean activo;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp  @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
}
