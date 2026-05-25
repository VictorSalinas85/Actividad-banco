package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatEstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatEstadoPrestamoRepository extends JpaRepository<CatEstadoPrestamo, Long> {
    Optional<CatEstadoPrestamo> findByCodigo(String codigo);
    Optional<CatEstadoPrestamo> findByCodigoAndActivoTrue(String codigo);
}
