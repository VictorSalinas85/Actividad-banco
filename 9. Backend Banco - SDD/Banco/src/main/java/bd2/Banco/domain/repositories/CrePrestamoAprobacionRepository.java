package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CrePrestamoAprobacion;
import bd2.Banco.domain.enums.DecisionAprobacionPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrePrestamoAprobacionRepository extends JpaRepository<CrePrestamoAprobacion, Long> {
    List<CrePrestamoAprobacion> findByPrestamoIdOrderByCreatedAtDesc(Long prestamoId);
    Optional<CrePrestamoAprobacion> findTopByPrestamoIdOrderByCreatedAtDesc(Long prestamoId);
    List<CrePrestamoAprobacion> findByDecision(DecisionAprobacionPrestamo decision);
    List<CrePrestamoAprobacion> findByAnalistaUsuarioId(Long analistaUsuarioId);
    boolean existsByPrestamoId(Long prestamoId);
}
