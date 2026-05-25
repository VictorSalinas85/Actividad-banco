package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatTipoIdentificacion;
import bd2.Banco.domain.enums.AplicaTipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatTipoIdentificacionRepository extends JpaRepository<CatTipoIdentificacion, Long> {
    Optional<CatTipoIdentificacion> findByCodigo(String codigo);
    List<CatTipoIdentificacion> findByActivoTrue();
    List<CatTipoIdentificacion> findByAplicaAAndActivoTrue(AplicaTipoIdentificacion aplicaA);
}
