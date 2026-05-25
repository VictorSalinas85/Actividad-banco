package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatEstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatEstadoCuentaRepository extends JpaRepository<CatEstadoCuenta, Long> {
    Optional<CatEstadoCuenta> findByCodigo(String codigo);
    Optional<CatEstadoCuenta> findByCodigoAndActivoTrue(String codigo);
}
