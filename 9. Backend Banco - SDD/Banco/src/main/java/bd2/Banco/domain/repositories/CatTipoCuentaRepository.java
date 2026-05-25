package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatTipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatTipoCuentaRepository extends JpaRepository<CatTipoCuenta, Long> {
    Optional<CatTipoCuenta> findByCodigo(String codigo);
    List<CatTipoCuenta> findByActivoTrue();
}
