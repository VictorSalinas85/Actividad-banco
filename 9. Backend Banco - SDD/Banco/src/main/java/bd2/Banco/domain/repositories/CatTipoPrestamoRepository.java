package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatTipoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatTipoPrestamoRepository extends JpaRepository<CatTipoPrestamo, Long> {
    Optional<CatTipoPrestamo> findByCodigo(String codigo);
    List<CatTipoPrestamo> findByActivoTrue();
}
