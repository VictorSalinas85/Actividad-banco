package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CrePrestamoDesembolso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrePrestamoDesembolsoRepository extends JpaRepository<CrePrestamoDesembolso, Long> {
    Optional<CrePrestamoDesembolso> findByPrestamoId(Long prestamoId);
    List<CrePrestamoDesembolso> findByCuentaDestinoId(Long cuentaDestinoId);
    boolean existsByPrestamoId(Long prestamoId);
}
