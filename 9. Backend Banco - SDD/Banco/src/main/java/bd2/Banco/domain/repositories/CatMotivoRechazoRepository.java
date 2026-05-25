package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatMotivoRechazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatMotivoRechazoRepository extends JpaRepository<CatMotivoRechazo, Long> {
    Optional<CatMotivoRechazo> findByCodigo(String codigo);
    Optional<CatMotivoRechazo> findByCodigoAndActivoTrue(String codigo);
    List<CatMotivoRechazo> findByActivoTrue();
}
