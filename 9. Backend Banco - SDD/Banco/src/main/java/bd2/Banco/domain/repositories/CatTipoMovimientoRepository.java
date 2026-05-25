package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatTipoMovimiento;
import bd2.Banco.domain.enums.NaturalezaMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatTipoMovimientoRepository extends JpaRepository<CatTipoMovimiento, Long> {
    Optional<CatTipoMovimiento> findByCodigo(String codigo);
    Optional<CatTipoMovimiento> findByCodigoAndActivoTrue(String codigo);
    List<CatTipoMovimiento> findByActivoTrue();
    List<CatTipoMovimiento> findByNaturalezaAndActivoTrue(NaturalezaMovimiento naturaleza);
}
