package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatParametroNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatParametroNegocioRepository extends JpaRepository<CatParametroNegocio, Long> {
    Optional<CatParametroNegocio> findByCodigo(String codigo);
    Optional<CatParametroNegocio> findByCodigoAndActivoTrue(String codigo);
}
