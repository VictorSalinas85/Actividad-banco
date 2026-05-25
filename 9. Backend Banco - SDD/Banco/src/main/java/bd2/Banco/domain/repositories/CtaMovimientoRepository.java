package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CtaMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CtaMovimientoRepository extends JpaRepository<CtaMovimiento, Long> {
    List<CtaMovimiento> findByCuentaIdOrderByFechaMovimientoDesc(Long cuentaId);
    Optional<CtaMovimiento> findByIdempotencyKey(String idempotencyKey);
    boolean existsByIdempotencyKey(String idempotencyKey);
    List<CtaMovimiento> findByCuentaIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
            Long cuentaId, LocalDateTime desde, LocalDateTime hasta);
}
