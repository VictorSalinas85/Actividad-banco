package bd2.Banco.domain.entities;

import bd2.Banco.domain.enums.ProductoTipo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "aud_bitacora_evento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AudBitacoraEvento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id") private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_operacion_id", nullable = false)
    private CatTipoOperacion tipoOperacion;

    @Column(name = "fecha_hora_operacion", nullable = false) private LocalDateTime fechaHoraOperacion;
    @Column(name = "id_usuario", nullable = false) private Long idUsuario;
    @Column(name = "rol_usuario_id") private Long rolUsuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "producto_tipo", nullable = false, length = 40) private ProductoTipo productoTipo;

    @Column(name = "producto_id", nullable = false, length = 64) private String productoId;

    /** JSON con el detalle del evento. Almacenado como texto. */
    @Column(name = "datos_detalle", nullable = false, columnDefinition = "json") private String datosDetalle;

    @Column(name = "hash_integridad", length = 64) private String hashIntegridad;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
}
