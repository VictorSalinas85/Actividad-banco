package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.AudBitacoraEvento;
import bd2.Banco.domain.enums.ProductoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudBitacoraEventoRepository extends JpaRepository<AudBitacoraEvento, Long> {
    List<AudBitacoraEvento> findByProductoTipoAndProductoIdOrderByFechaHoraOperacionDesc(
            ProductoTipo productoTipo, String productoId);
    List<AudBitacoraEvento> findByIdUsuarioOrderByFechaHoraOperacionDesc(Long idUsuario);
    List<AudBitacoraEvento> findByProductoTipoOrderByFechaHoraOperacionDesc(ProductoTipo productoTipo);
}
