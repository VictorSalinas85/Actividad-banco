package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatTipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatTipoOperacionRepository extends JpaRepository<CatTipoOperacion, Long> {
    Optional<CatTipoOperacion> findByCodigo(String codigo);
    Optional<CatTipoOperacion> findByCodigoAndActivoTrue(String codigo);
}
