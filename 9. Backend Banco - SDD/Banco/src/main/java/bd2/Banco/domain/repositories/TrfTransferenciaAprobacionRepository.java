package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.TrfTransferenciaAprobacion;
import bd2.Banco.domain.enums.DecisionAprobacionTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrfTransferenciaAprobacionRepository extends JpaRepository<TrfTransferenciaAprobacion, Long> {
    List<TrfTransferenciaAprobacion> findByTransferenciaIdOrderByCreatedAtDesc(Long transferenciaId);
    Optional<TrfTransferenciaAprobacion> findTopByTransferenciaIdOrderByCreatedAtDesc(Long transferenciaId);
    List<TrfTransferenciaAprobacion> findByDecision(DecisionAprobacionTransferencia decision);
    List<TrfTransferenciaAprobacion> findByAprobadorUsuarioId(Long aprobadorUsuarioId);
}
