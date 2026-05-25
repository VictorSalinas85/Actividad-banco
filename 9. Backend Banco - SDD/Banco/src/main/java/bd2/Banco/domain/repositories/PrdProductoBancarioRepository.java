package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.PrdProductoBancario;
import bd2.Banco.domain.enums.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrdProductoBancarioRepository extends JpaRepository<PrdProductoBancario, Long> {
    Optional<PrdProductoBancario> findByCodigo(String codigo);
    Optional<PrdProductoBancario> findByCodigoAndActivoTrue(String codigo);
    List<PrdProductoBancario> findByActivoTrue();
    List<PrdProductoBancario> findByCategoriaAndActivoTrue(CategoriaProducto categoria);
}
