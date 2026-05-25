package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatMotivoBloqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatMotivoBloqueoRepository extends JpaRepository<CatMotivoBloqueo, Long> {
    Optional<CatMotivoBloqueo> findByCodigo(String codigo);
    Optional<CatMotivoBloqueo> findByCodigoAndActivoTrue(String codigo);
    List<CatMotivoBloqueo> findByActivoTrue();
}
