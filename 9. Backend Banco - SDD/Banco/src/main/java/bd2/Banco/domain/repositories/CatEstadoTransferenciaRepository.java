package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatEstadoTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatEstadoTransferenciaRepository extends JpaRepository<CatEstadoTransferencia, Long> {
    Optional<CatEstadoTransferencia> findByCodigo(String codigo);
    Optional<CatEstadoTransferencia> findByCodigoAndActivoTrue(String codigo);
}
