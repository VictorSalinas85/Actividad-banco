package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatCanalOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatCanalOperacionRepository extends JpaRepository<CatCanalOperacion, Long> {
    Optional<CatCanalOperacion> findByCodigo(String codigo);
    Optional<CatCanalOperacion> findByCodigoAndActivoTrue(String codigo);
    List<CatCanalOperacion> findByActivoTrue();
}
